package uk.co.risi.datalake.aspect;

import uk.co.risi.datalake.domain.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@Aspect
@Component
public class DataLakeAspect {

    private Logger logger = Logger.getLogger(getClass().getName());

    @Pointcut("execution(* uk.co.risi.datalake.service.DataLakeService.ingest*(..))")
    private void dataEnrichment() {
    }

    @Before("dataEnrichment()")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args)
            if (arg instanceof Data) {
                ((Data) arg).setUuid(UUID.randomUUID());
                ((Data) arg).setTimestamp(LocalDateTime.now());
                logger.info("Enriching data for " + arg);
                break;
            }
    }
}
