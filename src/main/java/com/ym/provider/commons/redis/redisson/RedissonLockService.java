package com.ym.provider.commons.redis.redisson;

import java.util.concurrent.TimeUnit;

/**
 * @author ymaster1
 * @date 2020/11/11 10:24
 * @description redisson 分布式锁
 */
public interface RedissonLockService {
    /**
     * 加锁
     * @param key
     * @return
     */
    public Boolean lock(String key);

    /**
     * 带超时时间加锁
     * @param key
     * @param waitTime
     * @return
     */
    public Boolean lock(String key,Long waitTime);

    /**
     * 带超时时间加锁
     * @param key
     * @param waitTime
     * @param unit
     * @return
     */
    public Boolean lock(String key, Long waitTime, TimeUnit unit);

    /**
     * 尝试获取锁，在给定的waitTime时间内尝试，获取到返回true,获取失败返回false,获取到后再给定的leaseTime时间超时释放
     * @param key
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     */
    public Boolean lock(String key, Long waitTime,Long leaseTime, TimeUnit unit);

    /**
     * 释放锁
     * @param key
     * @return
     */
    void  unLock(String key);
}
