package cn.darkrong.multiredis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * 主要redis配置
 *
 * @author Rong.Jia
 * @date 2023/08/15
 */
@Configuration
public class PrimaryRedisConfig extends CachingConfigurerSupport {

    private final Duration timeToLive = Duration.ofSeconds(60);

    @Qualifier("primaryRedis")
    private final RedisConnectionFactory redisConnectionFactory;
    private final CacheProperties cacheProperties;

    public PrimaryRedisConfig(RedisConnectionFactory redisConnectionFactory, CacheProperties cacheProperties) {
        this.redisConnectionFactory = redisConnectionFactory;
        this.cacheProperties = cacheProperties;
    }


    /**
     * 在没有指定缓存Key的情况下，key生成策略
     *
     * @return {@link KeyGenerator} key生成策略
     */
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    /**
     * 缓存管理器
     *
     * @return {@link CacheManager} 缓存管理器
     */
    @Override
    public CacheManager cacheManager() {
        Jackson2JsonRedisSerializer<Object> redisValueSerializer = redisValueSerializer();
        StringRedisSerializer redisKeySerializer = redisKeySerializer();

        //关键点，spring cache的注解使用的序列化都从这来，没有这个配置的话使用的jdk自己的序列化，实际上不影响使用，只是打印出来不适合人眼识别
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()

                //key序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisKeySerializer))

                //value序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisValueSerializer))

                .disableCachingNullValues()

                //缓存过期时间
                .entryTtl((cacheProperties.getRedis() == null
                        || cacheProperties.getRedis().getTimeToLive() == null
                        || cacheProperties.getRedis().getTimeToLive().isZero())
                        ? timeToLive : cacheProperties.getRedis().getTimeToLive());

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(config)
                .transactionAware();

        return builder.build();
    }

    /**
     * RedisTemplate配置 在单独使用redisTemplate的时候 重新定义序列化方式
     *
     * @param redisConnectionFactory 连接工厂
     * @return RedisTemplate<String, Object>
     */
    @Bean("rtCliPri")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("primaryRedis") RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();

        Jackson2JsonRedisSerializer<Object> redisValueSerializer = redisValueSerializer();
        // value值的序列化采用fastJsonRedisSerializer
        template.setValueSerializer(redisValueSerializer);
        template.setHashValueSerializer(redisValueSerializer);

        StringRedisSerializer redisKeySerializer = redisKeySerializer();
        template.setHashKeySerializer(redisKeySerializer);

        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(redisKeySerializer);

        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();

        return template;
    }

    private Jackson2JsonRedisSerializer<Object> redisValueSerializer(){
        Jackson2JsonRedisSerializer<Object> redisValueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        redisValueSerializer.setObjectMapper(om);
        return redisValueSerializer;
    }

    private StringRedisSerializer redisKeySerializer() {
        return new StringRedisSerializer();
    }

    /**
     * 注入封装RedisTemplate
     *
     * @param redisTemplate {@link RedisTemplate}
     * @return {@link RedisUtils}
     */
    @Bean(name = "redisCliPri")
    public RedisUtils redisCliPri(@Qualifier("rtCliPri") RedisTemplate<String, Object> redisTemplate) {
        RedisUtils redisUtils = new RedisUtils();
        redisUtils.setRedisTemplate(redisTemplate);
        return redisUtils;
    }
}