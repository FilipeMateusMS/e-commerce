package com.project.api.ecommerce.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ServiceLoggingAspect.class);

    @Around("execution(* com.project.api.ecommerce.service..*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {

        String metodo = joinPoint.getSignature().toShortString();
        long inicio = System.currentTimeMillis();

        try {
            Object resultado = joinPoint.proceed();
            long tempo = System.currentTimeMillis() - inicio;

            if (tempo > 500) {
                log.warn("Service lento {} tempo={}ms", metodo, tempo);
            } else {
                log.debug("Service {} tempo={}ms", metodo, tempo);
            }

            return resultado;

        } catch (Exception ex) {
            log.error("Erro em {}", metodo, ex);
            throw ex;
        }
    }
}
