package com.example.aspect;
/*
    面向切面编程
 */
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Administrator on 2017/4/25.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);
        @Before("execution(* com.example.controller.*Controller.*(..))")
        public void beforMethod(JoinPoint joinPoint){/*切点*/
            StringBuilder sb=new StringBuilder();
            for(Object arg:joinPoint.getArgs()){
                sb.append("arg:"+arg.toString()+"|");
            }
            logger.info("before method:"+sb.toString());
        }
        @After("execution(* com.example.controller.IndexController.*(..))")
        public void afterMethod(){

            logger.info("after method"+new Date());
        }
}
