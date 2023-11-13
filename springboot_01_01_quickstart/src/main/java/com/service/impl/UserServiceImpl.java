package com.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dto.LoginDto;
import com.dto.ResultDto;
import com.entity.User;
import com.mapper.UserMapper;
import com.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.utils.RegexUtils;

import javax.servlet.http.HttpSession;

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
        if (cachePhone == null || cachePhone != phone ){
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
        if (loginDto.getPhone() == user.getPhone()){
            
        }
            return null;
    }
}
