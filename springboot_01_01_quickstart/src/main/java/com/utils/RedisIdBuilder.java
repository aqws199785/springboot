package com.utils;


import javafx.util.Builder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import com.utils.SystemConstants.*;

import static com.utils.SystemConstants.BEGIN_TIMESTAMP;

/*
 * Redis ID 生成器
 * 使用redis实现全局id生成器
 * id在业务中唯一
 * */
@Component
public class RedisIdBuilder {

    private StringRedisTemplate stringRedisTemplate;

    public RedisIdBuilder(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public long builder(String keyPrefix) {
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timeDiff = nowSecond - BEGIN_TIMESTAMP;
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long increment = stringRedisTemplate.opsForValue().increment(keyPrefix + ":" + date);
        // long 类型由8byte   1byte=8bit
        // long类型64bit中首位决定符号:正负
        // 接着31bit存储 timeDiff:时间差
        // 最后32bit存储 increment
        // 将timeDiff向前移动32位 最后面的32位全为0 接着喝 increment做与运算，increment就拼接到long类型的最后32位里(increment 不能大于2的32次方)
        // 解决方法:在key后面加上日期 这样increment就会在固定的时间刷新
        long id = timeDiff << 32 | increment;
        return id;
    }
}
