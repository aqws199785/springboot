package com.example;

import com.bean.User;
import com.config.RedisConfig;
import com.config.RedisConfigTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;


@SpringBootTest
public class RedisTemplateTest {
    @Autowired
    private RedisConfigTest redisConfigTest;

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // spring mvc 默认的 json工具 你也可以使用fastJson
    private static final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void test() throws JsonProcessingException {
        //  TODO (1) 输出自定义的redis conf配置
        System.out.println(
                redisConfigTest.getHost() + "\n" +
                        redisConfigTest.getKey() + "\n" +
                        redisConfigTest.getPassword()
        );
        //  TODO (2) 使用redisTemplate 会自动帮助你完成序列化和反序列化 同时携带了一个新的属性进入（类的字节码）
        User user = new User("张三", 100);
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.opsForValue().set("user", user);
        Object userStr1 = redisTemplate.opsForValue().get("user");
        System.out.println("RedisTemplate:" + userStr1);

        //  TODO (3) 使用StringRedisTemplate 需要你手动完成序列化与反序列化 与普通相比减少了存储空间
        // 手动序列化
        String json = mapper.writeValueAsString(user);
        stringRedisTemplate.opsForValue().set("user:string", json);
        String userStr2 = stringRedisTemplate.opsForValue().get("user:string");
        System.out.println("StringRedisTemplate" + userStr2);


    }
}

/*
* 控制台输出
person101
pattern
123
RedisTemplate:User(name=张三, age=100)
StringRedisTemplate{"name":"张三","age":100}
*
* redis中存储的数据
person101:6379> get user
{"@class":"com.bean.User","name":"张三","age":100}
*
person101:6379> get user:string
{"name":"张三","age":100}
*
* */
















































