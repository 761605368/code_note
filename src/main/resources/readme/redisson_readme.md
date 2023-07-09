#pom依赖
```
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.17.3</version>
</dependency>
```

#[配置文件](https://github.com/761605368/code_notes/blob/master/src/main/resources/application-redisson.yaml)
```
spring:
  redis:
    host: ${redis.host}
    password: ${redis.password}
    database: 1
```
#[使用](https://github.com/761605368/code_notes/blob/master/src/test/java/com/baidu/code_notes/redis/RedissonTest.java)
```
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

        // 尝试获取锁
        RLock lock = redissonClient.getLock("redissonTest");
        
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

        // 尝试获取锁
        RLock lock = redissonClient.getLock("redissonTest");

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
```
