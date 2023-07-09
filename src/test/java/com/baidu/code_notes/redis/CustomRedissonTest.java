package com.baidu.code_notes.redis;

import com.baidu.code_notes.utils.CustomRedisson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author lxh
 * @date 2023/7/9 11:50
 */
@SpringBootTest
@Slf4j
public class CustomRedissonTest {
    @Test
    @SneakyThrows
    public void redissonTest() {
        new Thread(this::machineOne).start();
        new Thread(this::machineTwo).start();

        TimeUnit.SECONDS.sleep(100);
    }

    @SneakyThrows
    public void machineOne() {

        // 尝试获取锁
        log.info("尝试获取锁");
        CustomRedisson.lock("redissonTest", 30, 50);
        log.info("获取锁成功");

        // 执行业余逻辑1
        log.info("执行业余逻辑1");
        TimeUnit.SECONDS.sleep(10);

        log.info("结束执行业余逻辑1");
        CustomRedisson.unlock("redissonTest");
        log.info("解锁成功");

    }

    @SneakyThrows
    public void machineTwo(){

        // 尝试获取锁
        log.info("尝试获取锁");
        CustomRedisson.lock("redissonTest", 30, 50);
        log.info("获取锁成功");

        // 执行业余逻辑1
        log.info("执行业余逻辑2");
        TimeUnit.SECONDS.sleep(10);

        log.info("结束执行业余逻辑2");
        CustomRedisson.unlock("redissonTest");
        log.info("解锁成功");

    }
}
