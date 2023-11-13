package com.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.dto.LoginDto;
import com.dto.ResultDto;
import com.entity.User;
import javax.servlet.http.HttpSession;

public interface UserService extends IService<User> {
    ResultDto sendCode(String phone, HttpSession session);

    ResultDto login(LoginDto loginDto, HttpSession httpSession);
}
