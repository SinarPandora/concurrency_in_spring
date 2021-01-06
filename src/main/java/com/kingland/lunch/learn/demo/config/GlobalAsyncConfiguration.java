package com.kingland.lunch.learn.demo.config;

import lombok.extern.java.Log;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Author: sinar
 * 2021/1/7 00:05
 */
@Log
@Configuration
public class GlobalAsyncConfiguration implements AsyncConfigurer {
    /**
     * The {@link AsyncUncaughtExceptionHandler} instance to be used
     * when an exception is thrown during an asynchronous method execution
     * with {@code void} return type.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable ex, Method method, Object... params) -> log.warning(ex.getMessage());
    }
}
