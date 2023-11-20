package com.utils;

import com.dto.UserDto;
import com.entity.User;

/*
 *   用户持有人
 *   线程本地的 增删改
 * */
public class UserHolder {
    private static final ThreadLocal<UserDto> threadLocal = new ThreadLocal<UserDto>();

    public static void saveUser(UserDto userDto) {
        threadLocal.set(userDto);
    }

    public static UserDto getUser() {
        return threadLocal.get();
    }

    public static void removeUser() {
        threadLocal.remove();
    }
}