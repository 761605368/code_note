package com.baidu.code_notes.redis;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author lxh
 * @date 2023/7/8 22:31
 */
@SpringBootTest
@Slf4j
public class RedissonTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    @SneakyThrows
    public void redissonTest() {
        this.machineOne();
        new Thread(this::machineOne).start();
        new Thread(this::machineTwo).start();

        TimeUnit.SECONDS.sleep(100);
    }

    @SneakyThrows
    public void machineOne() {

        RLock lock = redissonClient.getLock("redissonTest");

        // 尝试获取锁
        log.info("尝试获取锁");
        lock.lock();
        log.info("获取锁成功");

        // 执行业余逻辑1
        log.info("执行业余逻辑1");
        TimeUnit.SECONDS.sleep(10);

        log.info("结束执行业余逻辑1");
        lock.unlock();
        log.info("解锁成功");

    }

    @SneakyThrows
    public void machineTwo(){

        RLock lock = redissonClient.getLock("redissonTest");

        // 尝试获取锁
        log.info("尝试获取锁");
        lock.lock();
        log.info("获取锁成功");

        // 执行业余逻辑1
        log.info("执行业余逻辑2");
        TimeUnit.SECONDS.sleep(10);

        log.info("结束执行业余逻辑2");
        lock.unlock();
        log.info("解锁成功");

    }
}
