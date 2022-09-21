package com.example.demo.annotation;/*

 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginAnnotation {
    String name();//所调用接口的名称
    boolean intoDb() default false;//标识该条操作日志是否需要持久化存储
}
