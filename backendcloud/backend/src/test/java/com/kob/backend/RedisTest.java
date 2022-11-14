package com.kob.backend;

import com.kob.backend.utils.redis.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisTemplateTests {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void redisTest() {
        String key = "slogan";
        // 操作 String 类型
        redisUtil.set(key, "grow up");
        System.out.println("slogan 在 redis 中创建完毕");
        String value = (String) redisUtil.get(key);
        System.out.println("从 redis 中获得 2022 slogan 是：" + value);

    }
}