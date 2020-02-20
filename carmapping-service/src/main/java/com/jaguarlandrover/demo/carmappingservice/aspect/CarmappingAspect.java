package com.jaguarlandrover.demo.carmappingservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CarmappingAspect {

    @Pointcut("execution(* com.jaguarlandrover.demo.carmappingservice.config.endpoints.*Endpoints.get*())")
    private void createUrlForEndpoints() {}

    @Around("createUrlForEndpoints()")
    public String afterGetEndpoint(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            return "http://" + proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
