package com.ym.provider.commons.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singletonMap;

/**
 * @author ymaster1
 * @date 2020/9/24 15:34
 * @description
 */
@Configuration
@EnableCaching
//public class RedisConfig extends CachingConfigurerSupport {
public class RedisConfig  {
    /**
     * 缓存空间名称,可以让他从nacos上获取
     */
    @Value("${ext.name}")
    private String userCacheName;
    /**
     * name = redisTemplate ,此时会替换掉默认的RedisTemplate<Object, Object>
     * 实现自定义序列化
     *
     * @param factory LettuceConnectionFactory（已经自动配置）
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        //Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer<>(Object
        // .class);
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer =
                new GenericJackson2JsonRedisSerializer();

        //值采用json序列化
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        //设置redis支持数据库的事务
        template.setEnableTransactionSupport(true);
        // 设置list key 和value序列化模式
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 会替代默认的CacheManager，也可以注册多个，但是必须指定一个默认的@Pramiry
     * 使用缓存注解时，不指定就会用该CacheManager
     * 使用redisTemplate操作缓存不会经过这里
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
//        添加额外的缓存空间
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add(userCacheName);
        cacheNames.add("ymaster1");
        //设置CacheManager的值序列化方式为GenericJackson2JsonRedisSerializer，默认使用JdkSerializationRedisSerializer
        RedisSerializationContext.SerializationPair<Object> pair =
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());
//        设置redisCacheWriter
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(factory);
//        默认的config，如果指定的value不是配置过的缓存空间比如ymaster1，就会使用这个，
        RedisCacheConfiguration defaultCacheConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeValuesWith(pair)
//                        默认设置过期时间1小时，对于有的特殊key可以直接用redisTemplate操作设置单独过期时间，也可以再设置一个config
                        .entryTtl(Duration.ofHours(1))
//                        不缓存null，但是有时候需要缓存null预防击穿
                        .disableCachingNullValues();


//       可以配置多个RedisCacheConfiguration，个性化一些需求，比如过期时间，应用于不同名称的缓存空间
        Map<String,RedisCacheConfiguration> map = new HashMap<>(16);

//        指定userCacheName缓存空间的所有缓存过期时间为10分钟
        map.put(userCacheName,RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(pair)
//                        设置过期时间10分钟
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues());

//        指定ymaster1缓存空间的所有缓存过期时间为5分钟
        map.put("ymaster1",getRedisCacheConfigurationWithTtl(300));

// 生成RedisCacheManager，可以使用构造方法，也可以使用builder
//        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter,
//                defaultCacheConfig,map);
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(defaultCacheConfig)
//                让其他缓存空间生效，试了，加不加都一样
//                .initialCacheNames(cacheNames)
//                添加额外的config使其生效
                .withInitialCacheConfigurations(map)
                .build();
        return cacheManager;
    }

    /**
     * 生成一个除了过期时间其他都一样的config
     * @param seconds
     * @return
     */
    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofSeconds(seconds))
                .disableCachingNullValues();
        return redisCacheConfiguration;
    }



        /**
         * 自定义缓存key生成策略，默认是以方法加参数作为key,但是如果不同的包下的方法签名一样，就可能互相错读
         * @return
         * 此时，缓存的key是包名+方法名+参数列表，这样就很难会冲突了
         * 如果需要使其生效就必须继承CachingConfigurerSupport，不如不配置这个可以不用继承
         */
//    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }
}
