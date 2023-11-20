package com.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.LoginDto;
import com.dto.ResultDto;
import com.dto.UserDto;
import com.entity.User;
import com.mapper.UserMapper;
import com.service.UserService;
import com.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.utils.RegexUtils;

import javax.servlet.http.HttpSession;

import static com.utils.SystemConstantsUtils.USER_NICK_NAME_PREFIX;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public ResultDto sendCode(String phone, HttpSession session) {
        //  校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            return ResultDto.fail("手机号不符合格式 请查看你的手机号是否输入正确");
        }
        //  生成验证码
        String verifyCode = RandomUtil.randomNumbers(6);
        //  保存验证码 手机号到会话
        session.setAttribute("verifyCode", verifyCode);
        session.setAttribute("phone", phone);
        //  发送验证码 需要第三方工具较为麻烦 采用日志记录的方式实现
        log.info("发送短信验证码成功：{}", verifyCode);
        return ResultDto.ok();
    }

    /*
     * 登录注册合二为一
     * */
    @Override
    public ResultDto login(LoginDto loginDto, HttpSession httpSession) {
        if (RegexUtils.isPhoneInvalid(loginDto.getPhone())) {
            return ResultDto.fail("手机号格式错误");
        }
        String cachePhone = httpSession.getAttribute("phone").toString();
        String phone = loginDto.getPhone();
        // 验证验证码是否和会话中相同
        if (cachePhone == null || !cachePhone.equals(phone)) {
            return ResultDto.fail("手机号错误");
        }
        Object cacheVerifyCode = httpSession.getAttribute("verifyCode");
        String verifyCode = loginDto.getVerifyCode();
        if (cacheVerifyCode == null && cacheVerifyCode.toString() != verifyCode) {
            return ResultDto.fail("验证码错误");
        }
        // UserMapper继承baseMapper<User> 因此query() 可以查User对象指定的表
        // query() 相当于select * from table_name
        User user = query().eq("phone", loginDto.getPhone()).one();
        // 如果用户不存在 创建新用户
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
        System.out.println(loginDto.getPhone());
        System.out.println(user.toString());
        // 如果前端请求的手机号和数据库中的相同 保存到httpSession中
        if (loginDto.getPhone().equals(user.getPhone())) {
            httpSession.setAttribute("user",userDto);
            System.out.println("保存到会话");
        }
        return ResultDto.ok();
    }
}
