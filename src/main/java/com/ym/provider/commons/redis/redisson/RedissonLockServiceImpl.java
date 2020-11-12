package com.ym.provider.commons.redis.redisson;

import com.ym.provider.commons.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author ymaster1
 * @date 2020/11/11 10:28
 * @description
 */
@Slf4j
@Service
public class RedissonLockServiceImpl implements RedissonLockService, InitializingBean {
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;
    private RedissonClient client;

    /**
     * 默认等待时间(30s)
     */
    private static final long DEFAULT_WAIT_TIME = 30L;
    /**
     * 默认释放锁时间（10分钟）
     * 现有的业务应该都不会超过10分钟
     */
    private static final long DEFAULT_LEASE_TIME = 600L;

    @Override
    public Boolean lock(String key) {
        return this.lock(key, DEFAULT_WAIT_TIME);
    }

    @Override
    public Boolean lock(String key, Long waitTime, Long leaseTime, TimeUnit unit) {
        try {
            RLock lock = client.getLock(key);
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            throw new MyException("获取锁失败");
        }

    }

    @Override
    public Boolean lock(String key, Long waitTime, TimeUnit unit) {
        return this.lock(key, waitTime, DEFAULT_LEASE_TIME, unit);
    }

    @Override
    public Boolean lock(String key, Long waitTime) {
        return this.lock(key, waitTime, TimeUnit.SECONDS);
    }

    @Override
    public void unLock(String key) {
        RLock lock = client.getLock(key);
        lock.unlock();
    }

    @Override
    public void afterPropertiesSet() {
        Config config = new Config();
//        现在只有单机模式
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + host + ":" + port);
        singleServerConfig.setDatabase(database);
        if (password != null && !"".equals(password)) {
            singleServerConfig.setPassword(password);
        }
        this.client = Redisson.create(config);
        log.info("redisson客户端已创建！");
    }
}
