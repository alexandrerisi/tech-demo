package com.risi.demo.carmappingservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CarmappingAspect {

    @Value("${protocol}")
    private String protocol;

    @Pointcut("execution(* com.risi.demo.carmappingservice.config.endpoints.*Endpoints.get*())")
    private void createUrlForEndpoints() {}

    @Around("createUrlForEndpoints()")
    public String afterGetEndpoint(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            return protocol + proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
