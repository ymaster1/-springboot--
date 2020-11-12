package com.ym.provider.commons.tkmybais;

import com.ym.provider.commons.utils.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author ymaster1
 * @date 2020/9/24 17:03
 * @description
 * Mybatis默认没有开启二级缓存需要在setting全局参数中配置开启二级缓存
 * 自定义mybatis 二级缓存
 * 在mapper.xml中的<cache/>标签中用type指向此类即可
 * 一般不用二级缓存，都直接在service用spring cache，除非特别简单的场景会使用
 * <cache type="com.ym.provider.commons.tkmybais.tkCache"/>
 *
 * update 句子设置flushCache="true"，表示让缓存失效
 */
@Slf4j
public class tkCache implements Cache {

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id;
    /**
     * 不能通过autowire的方式引用redisTemplate，因为RedisCache并不是Spring容器里的bean
     */
    private RedisTemplate<String,Object> redisTemplate = ApplicationContextHolder.getBean("redisTemplate");
    /**
     * redis过期时间
     */
    private static final long EXPIRE_TIME_IN_MINUTES = 30L;

    /**
     * 自己实现的二级缓存，必须要有一个带id的构造函数，否则会报错。
     * @param id
     */
    public tkCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        } else {
            log.info("cache init ... cache id [{}]", id);
            this.id = id;
        }
    }
    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    /**
     * mybatis缓存操作对象的标识符。一个mapper对应一个mybatis的缓存操作对象。
     *
     * @return
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 将查询结果塞入缓存
     *
     * @param key
     * @param value
     */
    @Override
    public void putObject(Object key, Object value) {
        try {
            getRedisTemplate();
            this.redisTemplate.opsForValue().set(key.toString(), value);
            this.redisTemplate.expire(this.id, EXPIRE_TIME_IN_MINUTES, TimeUnit.MINUTES);
            log.debug("Put query result to redis");
        } catch (Throwable t) {
            log.error("Redis put failed: {}", t.getMessage());
        }
    }

    /**
     * 从缓存中获取被缓存的查询结果
     *
     * @param key
     * @return
     */
    @Override
    public Object getObject(Object key) {
        try {
            log.debug("Get cached query result from redis");
            getRedisTemplate();
            return this.redisTemplate.opsForValue().get(key.toString());
        } catch (Throwable t) {
            log.error("Redis get failed: {}", t.getMessage());
            return null;
        }

    }

    /**
     * 从缓存中删除对应的key、value。只有在回滚时触发。一般我们也可以不用实现
     *
     * @param key
     * @return
     */
    @Override
    public Object removeObject(Object key) {
        try {
            getRedisTemplate();
            this.redisTemplate.delete(key.toString());
            log.info("Remove cached query result from redis");
        } catch (Throwable var3) {
            log.error("Redis remove failed: {}", var3.getMessage());
        }

        return null;
    }

    /**
     * 发生更新时，清除缓存。
     */
    @Override
    public void clear() {
        try {
            getRedisTemplate();
            log.info("Clear redis cache by key {}", this.id);
            this.redisTemplate.delete(this.id);
        } catch (Exception var2) {
            log.error("Clear cached failed: {}", var2.getMessage());
        }
    }

    /**
     * 可选实现。返回缓存的数量
     *
     * @return
     */
    @Override
    public int getSize() {
        return 0;
    }

    /**
     * 获取RedisTemplate
     * @return
     */
    private RedisTemplate getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = ApplicationContextHolder.getBean("redisTemplate");
        }
        return redisTemplate;
    }

}
