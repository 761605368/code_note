package com.baidu.code_notes.utils;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author lxh
 * @date 2023/7/9 10:30
 */
@Slf4j
@Component
public class CustomRedisson {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static RedisTemplate<String, Object> redisTemplateStatic;

    public static final String MACHINE_ID = IdUtil.randomUUID();

    @PostConstruct
    public void init() {
        redisTemplateStatic = this.redisTemplate;
    }

    /**
     * 加锁
     *
     * @param key         key
     * @param expiredTime 过期时间
     * @param waitTime    等待时间
     * @return {@link Boolean}
     * @throws InterruptedException 中断异常
     */
    public static Boolean lock(String key, long expiredTime, long waitTime) throws InterruptedException {
        Assert.hasText(key, "key不能为空");
        key = key = CustomRedisson.getkey(key);

        long startTime = System.currentTimeMillis();
        while (true) {
            // 尝试获取锁
            Boolean lockFlag = redisTemplateStatic.opsForValue().setIfAbsent(key, 1, expiredTime, TimeUnit.SECONDS);
            if (lockFlag) {
                // 加上锁了
                return true;
            }

            // 判断是否超过等待时间
            if (waitTime >= 0) {
                if (System.currentTimeMillis() - startTime >= TimeUnit.SECONDS.toMillis(waitTime)) {
                    return false;
                }
            }

            // 未加上锁,等待一会儿
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    private static String getkey(String key) {
        Assert.hasText(key, "key不能为空");

        return key + "_" + MACHINE_ID + ":" +  Thread.currentThread().getId();
    }

    /**
     * 解锁
     *
     * @param key         key
     * @return {@link Boolean}
     */
    public static Boolean unlock(String key) {
        Assert.hasText(key, "key不能为空");
        key = CustomRedisson.getkey(key);

        // 尝试解锁
        return redisTemplateStatic.delete(key);
    }
}
