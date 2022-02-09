package uk.co.risi.gateway.aspect;

import uk.co.risi.gateway.exception.InsufficientVinPermissionException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Order(2)
public class GatewayAspect {

    @Pointcut("execution(* uk.co.risi.gateway.service.GatewayService.generate*Stream(..))")
    private void checkVinPermission(){}

    @Before("checkVinPermission()")
    public void beforeStream(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (!(arg instanceof String) || !hasVinPermission((String) arg))
                throw new InsufficientVinPermissionException("You don't have permission on this VIN.");
            break;
        }
    }

    private boolean hasVinPermission(String vin) {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .contains(new SimpleGrantedAuthority(vin));
    }
}
