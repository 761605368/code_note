#pom依赖
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
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

#[config](https://github.com/761605368/code_notes/blob/master/src/main/java/com/baidu/code_notes/config/RedisConfig.java)
```
@Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 方法过期，改为下面代码
        // objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        redisTemplate.setKeySerializer(stringRedisSerializer); // key的序列化类型
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); // value的序列化类型
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
```

#[自定分布式锁](https://github.com/761605368/code_notes/blob/master/src/main/java/com/baidu/code_notes/utils/CustomRedisson.java)
## 特别注意：@Autowired是按类型注入，如果没有自定义的RedisTemplate<String, Object>,可能会注入失败,也可以使用@Resource,这个javax提供的，按名称注入
```
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

```

#[自定分布式锁使用](https://github.com/761605368/code_notes/blob/master/src/test/java/com/baidu/code_notes/redis/CustomRedissonTest.java)
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
