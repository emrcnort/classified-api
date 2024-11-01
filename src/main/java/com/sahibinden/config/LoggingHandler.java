package com.sahibinden.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingHandler{

    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(LoggingHandler.class);
    private static final long THRESHOLD = 5;

    @Around("execution(* com.sahibinden.controller..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTime;

        if (duration > THRESHOLD) {
            String methodName = joinPoint.getSignature().getName();
            String className = joinPoint.getSignature().getDeclaringTypeName();
            Object[] args = joinPoint.getArgs();
            String requestArgs = objectMapper.writeValueAsString(args);
            logger.info("{}.{} executed in {} ms Request: {}", className,methodName, duration, requestArgs);
        }

        return proceed;
    }
}
