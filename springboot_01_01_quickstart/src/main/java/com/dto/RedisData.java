package com.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/*
* 逻辑过期锁需要的额外的返回值类型
* */
@Getter
@Setter
public class RedisData {
    /*
    * 逻辑过期的时间戳
    * */
    private LocalDateTime expireTimeStamp;
    /*
    * 存储数据
    * */
    private Object data;

}
