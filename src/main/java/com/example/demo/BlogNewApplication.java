package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan(basePackages = "com.example.demo.dao")
@EnableAspectJAutoProxy
public class BlogNewApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogNewApplication.class, args);
    }

}
