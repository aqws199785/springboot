package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


/*
* 自动生成get set 有参构造和无参构造方法的注解
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
    private Boolean success;
    private String errorMessage;
    private Object data;
    private Long total;

    public static ResultDto ok() {
        return new ResultDto(true, null, null, null);
    }

    public static ResultDto ok(Object data) {
        return new ResultDto(true, null, data, null);
    }

    public static ResultDto ok(List<?> data, Long total) {
        return new ResultDto(true, null, data, total);
    }

    public static ResultDto fail(String errorMessage) {
        return new ResultDto(false,errorMessage,null,null);
    }
}
