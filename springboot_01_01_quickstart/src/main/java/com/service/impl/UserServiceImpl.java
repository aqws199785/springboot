package com.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.LoginDto;
import com.dto.ResultDto;
import com.dto.UserDto;
import com.entity.User;
import com.mapper.UserMapper;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.utils.RegexUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.utils.RedisConstants.*;
import static com.utils.SystemConstants.USER_NICK_NAME_PREFIX;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    // @Resource注解是Java中用于注入资源的注解，
    // 通常被用于注入Java EE应用程序中的资源，如数据源、消息源、事务管理器等。
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ResultDto sendCode(String phone, HttpSession session) {
        //  校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            return ResultDto.fail("手机号不符合格式 请查看你的手机号是否输入正确");
        }
        //  生成验证码
        String verifyCode = RandomUtil.randomNumbers(6);

        //   (v2版本) 保存验证码 redis 并设置ttl
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, verifyCode, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        //  发送验证码 需要第三方工具较为麻烦 采用日志记录的方式实现
        log.info("发送短信验证码成功：{}", verifyCode);
        return ResultDto.ok();
    }

    /*
     * 登录注册合二为一
     * */
    @Override
    public ResultDto login(LoginDto loginDto, HttpSession httpSession) {
        // (1) 校验手机号
        if (RegexUtils.isPhoneInvalid(loginDto.getPhone())) {
            return ResultDto.fail("手机号格式错误");
        }

        // (V2) 从redis中获取验证码
        String cacheVerifyCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + loginDto.getPhone());
        String verifyCode = loginDto.getVerifyCode();
        if (cacheVerifyCode == null && cacheVerifyCode.equals(verifyCode)) {
            return ResultDto.fail("验证码错误");
        }
        // (4) 查询数据库查看用户是否存在
        // UserMapper继承baseMapper<User> 因此query() 可以查User对象指定的表
        // query() 相当于select * from table_name
        User user = query().eq("phone", loginDto.getPhone()).one();
        // 如果用户不存在 创建新用户并保存到数据库
        if (user == null) {
            user = new User();
            user.setPhone(loginDto.getPhone());
            user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
            save(user);
        }
        //  使用BeanUtil.copyProperties 将一个对象的属性复制到另一个对象 需要属性名称相同
        //  该方式效率较低 可以考虑使用 Mapstruct 参考:https://juejin.cn/post/7140149801991012365
//        UserDto userDto = BeanUtil.copyProperties(user, UserDto.class);
        UserDto userDto = new UserDto(user.getId(), user.getNickName(), user.getIcon());

        // （5）保存用户 需要前端请求的手机号和数据库中的相同
        if (loginDto.getPhone().equals(user.getPhone())) {
            //  (v2) 保存到redis
            // 生成token isSimple 为true 没有中划线拼接(该方法可能有碰撞 建议使用手机号加密等方式生成)
            String token = UUID.randomUUID().toString(true);
            // 对象转换为map (value需要为string类型,使用的StringRedisTemplate 全属性string)
            Map<String, Object> userDtoMap = BeanUtil.beanToMap(
                    userDto,
                    new HashMap<>(),
                    CopyOptions.create().setIgnoreNullValue(true)   // 忽略空值
                            .setFieldValueEditor((field, value) -> value.toString())
            );
            stringRedisTemplate.opsForHash().putAll(LOGIN_TOKEN_KEY + token, userDtoMap);
            stringRedisTemplate.expire(LOGIN_TOKEN_KEY + token, LOGIN_TOKEN_TTL, TimeUnit.MINUTES);
            System.out.println("保存到会话");
            // 修改为redis后不再返回userDto，改为返回token
            return ResultDto.ok(token);
        }
        return ResultDto.fail("输入的验证码与redis数据库中的验证码不匹配");
    }
}
