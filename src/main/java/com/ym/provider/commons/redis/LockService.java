package com.ym.provider.commons.redis;

/**
 * @author ymaster1
 * @date 2020/9/24 19:13
 * @description
 * 分布式锁，做幂等
 */
public interface LockService {
    /**
     * 上锁
     * @param key
     * @return
     */
    boolean lock(String key);

    /**
     * 释放锁
     * @param key
     * @return
     */
    boolean unLock(String key);
}
