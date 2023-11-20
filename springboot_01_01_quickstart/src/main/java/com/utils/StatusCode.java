package com.utils;

import lombok.Getter;

@Getter
public enum StatusCode {
    SUCCESS(200, "成功"),
    NOT_FOUND(404, "未找到"),
    NOT_EXIST(401,"不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误");

    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
