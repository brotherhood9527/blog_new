package com.example.demo.config;/*

 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisPoolConfiguration {
    @Value("${spring.redis.host:127.0.0.1}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private Integer port;

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(Integer.valueOf(1000));
        jedisPoolConfig.setMaxIdle(Integer.valueOf(32));
        jedisPoolConfig.setMaxWaitMillis(Integer.valueOf(1000000));
        jedisPoolConfig.setTestOnBorrow(Boolean.valueOf(true));
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setTestOnCreate(true);
        jedisPoolConfig.setTestOnReturn(true);
        return new JedisPool(jedisPoolConfig, host,port);
    }

}
