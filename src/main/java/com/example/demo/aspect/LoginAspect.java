package com.example.demo.aspect;/*

 */

import com.example.demo.annotation.LoginAnnotation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoginAspect {

    private static final Logger logger =  LoggerFactory.getLogger(LoginAspect.class);

    private ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    //定义一个切入点
    @Pointcut("@annotation(com.example.demo.annotation.LoginAnnotation)")
    public void loginPointcut(){

    }

    /**
     * 接受请求并记录数据
     * @param joinPoint
     * @param loginAnnotation
     */
    @Before("loginPointcut() && @annotation(loginAnnotation)")
    public void loginBefore(JoinPoint joinPoint, LoginAnnotation loginAnnotation){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        // 记录请求内容，threadInfo存储所有内容
        Map<String, Object> threadInfo = new HashMap<>();
        logger.info("URL : " + request.getRequestURL());
        threadInfo.put("url", request.getRequestURL());
        logger.info("URI : " + request.getRequestURI());
        threadInfo.put("uri", request.getRequestURI());
        logger.info("HTTP_METHOD : " + request.getMethod());
        threadInfo.put("httpMethod", request.getMethod());
        //IPV4的地址
        logger.info("REMOTE_ADDR : " + request.getRemoteAddr());
        threadInfo.put("ip", request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());
        threadInfo.put("classMethod",
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
        threadInfo.put("args", Arrays.toString(joinPoint.getArgs()));
        logger.info("USER_AGENT"+request.getHeader("User-Agent"));
        threadInfo.put("userAgent", request.getHeader("User-Agent"));
        logger.info("执行方法：" + loginAnnotation.name());
        threadInfo.put("methodName", loginAnnotation.name());
        threadLocal.set(threadInfo);
    }

    /**
     * 执行成功后进行处理，当执行方法有异常时，afterReturning不会执行
     * @param joinPoint
     * @param loginAnnotation
     * @param ret
     */
    @AfterReturning(value = "loginPointcut() && @annotation(loginAnnotation)", returning = "ret")
    public void loginAfterReturning(JoinPoint joinPoint, LoginAnnotation loginAnnotation, Object ret){
        Map<String, Object> threadInfo = threadLocal.get();
        threadInfo.put("result", ret);
        if (loginAnnotation.intoDb()) {
            //插入数据库操作
            //insertResult(threadInfo);
        }
        //处理完请求，返回内容
        logger.info("RESPONSE : " + ret);
    }

    /**
     * 记录目标方法的执行时间
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "loginPointcut()")
    public Object loginAroundTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object ob = proceedingJoinPoint.proceed();
        long startTime = System.currentTimeMillis();
        Map<String, Object> threadInfo = threadLocal.get();
        Long takeTime = System.currentTimeMillis() - startTime;
        threadInfo.put("takeTime", takeTime);
        logger.info("耗时：" + takeTime + "毫秒");
        threadLocal.set(threadInfo);
        return ob;
    }

    /**
     * 当目标方法发生异常时，进行日志记录
     * @param throwable
     */
    @AfterThrowing(value = "loginPointcut()", throwing = "throwable")
    public void doAfterThrowing(Throwable throwable) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        //异常信息
        logger.error("{}接口调用异常，异常信息{}", request.getRequestURI(), throwable);
    }

    //2.如何进行日志持久化
}
