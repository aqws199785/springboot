package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bean.User;
import com.dto.ResultDto;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public interface IUserService extends IService<User> {
    ResultDto sendCode(String phone, HttpSession session);
}
