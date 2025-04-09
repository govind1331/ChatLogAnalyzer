package com.projectx.loganalyzer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory){
	
	RedisTemplate<String, Object> template = new RedisTemplate<>();
	
	template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());         // Plain string keys
    template.setHashKeySerializer(new StringRedisSerializer());     // Plain string hash keys
    template.setHashValueSerializer(new StringRedisSerializer());   // Plain string hash values
    template.afterPropertiesSet();
	return template;
    }

}
