package com.jaguarlandrover.demo.gateway.aspect;

import com.jaguarlandrover.demo.gateway.exception.InsufficientVinPermissionException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(2)
public class GatewayAspect {

    @Pointcut("execution(* com.jaguarlandrover.demo.gateway.service.GatewayService.generate*Stream(..))")
    private void checkVinPermission() {
    }

    @Pointcut("execution(* com.jaguarlandrover.demo.gateway.configuration.endpoints.*Endpoints.get*())")
    private void createUrlForEndpoints() {
    }

    @Before("checkVinPermission()")
    public void beforeStream(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (!(arg instanceof String) || !hasVinPermission((String) arg))
                throw new InsufficientVinPermissionException("You don't have permission on this VIN.");
            break;
        }
    }

    @Around("createUrlForEndpoints()")
    public String afterGetEndpoint(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            return "http://" + proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private boolean hasVinPermission(String vin) {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .contains(new SimpleGrantedAuthority(vin));
    }
}
