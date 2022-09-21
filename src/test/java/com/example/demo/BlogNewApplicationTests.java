package com.example.demo;


import com.example.demo.config.JedisPoolConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
class BlogNewApplicationTests {

    @Test
    void contextLoads() {

    }
    
    @Test
    public void testJedisRegister(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JedisPoolConfiguration.class);
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String name : beanDefinitionNames){
            System.out.println(name);
        }
    }

}
