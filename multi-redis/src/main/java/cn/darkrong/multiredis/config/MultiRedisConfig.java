package cn.darkrong.multiredis.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * redis 多数据源配置
 *
 * @author Rong.Jia
 * @date 2023/08/15
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class MultiRedisConfig {

    @Primary
    @Bean(name = "primaryRedis")
    @ConfigurationProperties(prefix="spring.redis.primary")
    public RedisConnectionFactory primaryRedisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean(name = "secondaryRedis")
    @ConfigurationProperties(prefix="spring.redis.secondary")
    public RedisConnectionFactory secondaryRedisConnectionFactory() {
        return new LettuceConnectionFactory();
    }


}
