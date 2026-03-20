package com.project.api.ecommerce.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private long startTime;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        startTime = System.currentTimeMillis();

        log.info("HTTP {} {}?{}", request.getMethod(), request.getRequestURI(), request.getQueryString() );
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {

        long duration = System.currentTimeMillis() - startTime;

        log.info("Status={} Tempo={}ms URI={}",
                response.getStatus(),
                duration,
                request.getRequestURI());
    }
}
