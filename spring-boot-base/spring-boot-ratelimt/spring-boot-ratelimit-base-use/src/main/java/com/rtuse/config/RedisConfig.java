package com.rtuse.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置信息
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {

    @Bean("redisTemplate")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, String> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        // key 序列化方式: StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        // value 序列化方式: Jackson2JsonRedisSerializer
        template.setValueSerializer(getJacksonSerializer());

        template.afterPropertiesSet();
        return template;
    }


    @Bean("kryoRedisTemplate")
    public RedisTemplate<String, String> kryoRedisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, String> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        // 默认 JdkSerializationRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        // 默认 JdkSerializationRedisSerializer
        template.setValueSerializer(new KryoRedisSerializer<>(Object.class));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new KryoRedisSerializer<>(Object.class));

        template.afterPropertiesSet();
        return template;
    }

    private Jackson2JsonRedisSerializer<Object> getJacksonSerializer() {
        // key序列化方式: Jackson2JsonRedisSerializer, 默认使用JDK JdkSerializationRedisSerializer 的序列化方式
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        return serializer;
    }
}
