package com.baidu.code_notes.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 对ThreadLocal的封装，使其能够存入多个key-value(使用完记得clear)
 * 重点理解：
 * 1.这里threadLocal是一个key，作用是从Thread的ThreadLocalMap中通过key取出相应的value
 * 2.尽管这里的ThreadLocal是一个常量，当时当它从不同的Thread中去取value时，
 *  因为Thread的不同，导致ThreadLocalMap不同，从而取出的value不同（尽管key是相同，但是map不同啊）
 * @author lxh
 * @date 2022/2/26 17:32
 */
public class ThreadLocalUtil {

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(()-> new HashMap<>(8));

    /**
     * 获取存储数据的map
     * @return
     */
    private static Map<String, Object> getMap() {
        return THREAD_LOCAL.get();
    }

    /**
     * 存入key-value
     * @param key
     * @param value
     */
    public static void put(String key, Object value) {
        System.out.println(THREAD_LOCAL);
        getMap().put(key,value);
    }

    /**
     * 通过key获取value
     * @param key
     * @return
     */
    public static Object get(String key) {
        System.out.println(THREAD_LOCAL);
        return getMap().get(key);
    }

    /**
     * 根据key移除value
     * @param key
     * @return
     */
    public static Object remove(String key) {
        return getMap().remove(key);
    }

    /**
     * 移除整个map
     * 当线程执行完成一定要清空，防止内存泄漏和数据污染
     */
    public static void clear() {
        getMap().clear();
        THREAD_LOCAL.remove();
    }



}
