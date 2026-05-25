package com.xxq.dronerent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类
 * 配置 RedisTemplate 序列化方式
 *
 * @author xxq
 * @since 2024-01-01
 */
@Configuration
public class RedisConfig {

    /**
     * 配置 RedisTemplate
     * 使用 JSON 序列化方式，方便存储和读取对象
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用 StringRedisSerializer 来序列化和反序列化 redis 的 key 值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        
        // 使用 GenericJackson2JsonRedisSerializer 来序列化和反序列化 redis 的 value 值
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        // key 采用 String 的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash 的 key 也采用 String 的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value 序列化方式采用 jackson
        template.setValueSerializer(jsonRedisSerializer);
        // hash 的 value 序列化方式采用 jackson
        template.setHashValueSerializer(jsonRedisSerializer);
        
        template.afterPropertiesSet();

        return template;
    }
}
