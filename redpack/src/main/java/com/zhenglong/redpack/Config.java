package com.zhenglong.redpack;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author Lion
 * @描述
 * @date 2019/5/11
 */
@Configuration
public class Config {
    @Bean
    public RestTemplate getTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RedisTemplate redisTemplate() {
        return new RedisTemplate();
    }
}
