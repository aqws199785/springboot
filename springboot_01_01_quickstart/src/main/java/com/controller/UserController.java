package com.controller;

import com.dto.LoginDto;
import com.dto.ResultDto;
import com.dto.UserDto;
import com.entity.User;
import com.service.UserService;
import com.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {


    @Resource //依赖注入 可以在bean中注入其他的bean实例
    private UserService userService;

    /*
    * 发送手机验证码
    * */
    @PostMapping("/code")
    //  @RequestParam 发送请求时会将请求的值带上并且和参数phone进行绑定 url 会带上 phone=phone_value
    public ResultDto sendCode(@RequestParam("phone") String phone, HttpSession session){
        return userService.sendCode(phone,session);
    }

    /*
    * 用户登录
    * 登录需要的账户 验证码 密码 保存在请求体中 会话也保存这些信息
    * */
    @PostMapping("/login")
    public ResultDto login(@RequestBody LoginDto loginDto,HttpSession httpSession){
        return userService.login(loginDto,httpSession);
    }

    @GetMapping("/me")
    public ResultDto me(){
        UserDto userDto = UserHolder.getUser();
        return ResultDto.ok(userDto);
    }

}
