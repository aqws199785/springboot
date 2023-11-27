package com.Interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.dto.UserDto;
import com.entity.User;
import com.utils.StatusCode;
import com.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.utils.RedisConstants.LOGIN_TOKEN_KEY;
import static com.utils.RedisConstants.LOGIN_TOKEN_TTL;

public class LoginInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    // 日志拦截器为自己创建 没有注入到spring容器 因此创建构造方法
    // 在可以注入spring容器的类中使用 会一并注入
    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /*
     * 处理用户发送的请求 在请求发送到Controller之前
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token ,key:authorization在前端中设置
        // 没有token 就拦截
        String token = request.getHeader("authorization");

        if (StrUtil.isBlank(token)) {
            response.setStatus(StatusCode.NOT_EXIST.getCode());
            response.addHeader("errorMassage","token为空");
            return false;
        }

        String key = LOGIN_TOKEN_KEY + token.toString();
        System.out.println("key"+key);
        System.out.println(token.toString());
        // 获取redis中的userDto
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        if (userMap.isEmpty()) {
            response.setStatus(StatusCode.NOT_EXIST.getCode());
            return false;
        }

        UserDto userDto = BeanUtil.fillBeanWithMap(userMap, new UserDto(), false);

        // 存在 保存用户信息保存到ThreadLocal
        UserHolder.saveUser(userDto);

        stringRedisTemplate.expire(key,LOGIN_TOKEN_TTL, TimeUnit.MINUTES);

        System.out.println((userDto).toString());

        // 放行
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /*
     *   请求到达controller之后
     *   移除用户 避免内存泄漏
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
