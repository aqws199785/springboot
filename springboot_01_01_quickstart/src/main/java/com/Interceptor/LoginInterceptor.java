package com.Interceptor;

import com.dto.UserDto;
import com.entity.User;
import com.utils.StatusCode;
import com.utils.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    /*
     * 处理用户发送的请求 在请求发送到Controller之前
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        //  如果用户不存在 拦截
        if (user == null) {
            response.setStatus(StatusCode.NOT_EXIST.getCode());
            return false;
        }
        // 存在 保存用户信息保存到ThreadLocal
        UserHolder.saveUser((UserDto) user);

        System.out.println(((UserDto)user).toString());

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
