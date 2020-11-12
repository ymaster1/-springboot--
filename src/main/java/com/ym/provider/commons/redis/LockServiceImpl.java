package com.ym.provider.commons.redis;

import cn.hutool.core.lang.UUID;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author ymaster1
 * @date 2020/9/24 19:14
 * @description 分布式锁
 * 从 Redis 2.6.12 版本开始，SET 命令的行为可以通过一系列参数来修改
 * SETEX KEY seconds value 原子操作，设置key并设置时间（秒），当 KEY 存在时，会覆盖旧值  = SET KEY value EX second
 * 此时ex 仅仅只是起到设置时间的作用（不然会报错）
 * SET KEY value NX 效果等同于 SETNX KEY value 。
 * <p>
 * Redis从2.6之后支持setnx、setex连用 使用lua脚本，避免setnx后服务宕机，锁永不过期，可以使用 EVAL 命令对 Lua 脚本进行求值。
 */
@Service
@Slf4j
public class LockServiceImpl implements LockService {
    @Autowired
    private RedisUtils redisUtils;
    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 释放锁脚本
     */
    public static final String UNLOCK_LUA;
    /**
     * 默认过期时间(30ms)
     */
    private static final long DEFAULT_EXPIRE = 60L;


    /**
     * 释放锁脚本 实现只能释放自己的锁，原子操作
     */
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * 获取分布式锁，原子操作
     *
     * @param lockKey
     * @param requestId 唯一ID, 可以使用UUID.randomUUID().toString();
     * @param expire
     * @param timeUnit
     * @return
     */
    public boolean LockByConnection(String lockKey, String requestId, long expire, TimeUnit timeUnit) {
//        redisTemplate并没有直接提供5个参数的set，所以需要自己实现(jedis有)
        try {
//            最后一个参数设置NX,这个方法是原子操作吗
            RedisCallback<Boolean> callback = (connection) -> connection.set(lockKey.getBytes(Charset.forName("UTF-8")), requestId.getBytes(Charset.forName("UTF-8")), Expiration.seconds(timeUnit.toSeconds(expire)), RedisStringCommands.SetOption.SET_IF_ABSENT);
            return (Boolean) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("redis lock error.", e);
        }
        return false;
    }

    /**
     * 获取分布式锁，原子操作
     *
     * @param lockKey
     * @param requestId 唯一ID, 可以使用UUID.randomUUID().toString();
     * @param expire
     * @param timeUnit
     * @return 引入了redisson依赖就返回null   去掉这个就好了  不知道为啥
     */
    public boolean LockByRestTemplate(String lockKey, String requestId, long expire, TimeUnit timeUnit) {
//        redisTemplate并没有直接提供5个参数的set，所以需要自己实现(jedis有)
        try {

//            高版本的redisTemplate已经提供实现了NX EX的方法了，最后会调用头上那个方法，但他是原子的吗？
            return redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expire, timeUnit);
        } catch (Exception e) {
            log.error("redis lock error.", e);
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey
     * @param requestId 唯一ID
     * @return
     */
    public boolean unLockByConnection(String lockKey, String requestId) {
        RedisCallback<Boolean> callback = (connection) -> connection.eval(UNLOCK_LUA.getBytes(), ReturnType.BOOLEAN, 1, lockKey.getBytes(Charset.forName("UTF-8")), requestId.getBytes(Charset.forName("UTF-8")));
        return (Boolean) redisTemplate.execute(callback);
    }

    /**
     * 释放锁
     *
     * @param lockKey
     * @param requestId 唯一ID
     * @return
     */
    public boolean unLockByRedisTemplate(String lockKey, String requestId) {
        return false;
    }

    /**
     * 获取Redis锁的value值
     *
     * @param lockKey
     * @return
     */
    public String get(String lockKey) {
        try {
            RedisCallback<String> callback = (connection) -> {
                return new String(connection.get(lockKey.getBytes()), Charset.forName("UTF-8"));
            };
            return (String) redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("get redis occurred an exception", e);
        }
        return null;
    }

    @Override
    public boolean lock(String key) {
        return false;
    }

    @Override
    public boolean unLock(String key) {
        return false;
    }
}
