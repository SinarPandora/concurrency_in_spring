package com.kingland.lunch.learn.demo.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableScheduling
@EnableAsync // <- enable @Async
@Configuration
public class ExecutionContextConfiguration {
    @Bean("defaultExecutor")
    public Executor defaultExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        // ------------------- CONFIGURATION -----------------------//
        executor.setCorePoolSize(10);       // default is 1
        executor.setMaxPoolSize(20);        // default is Integer.MAX_VALUE
        executor.setQueueCapacity(50);      // default is Integer.MAX_VALUE
        executor.setKeepAliveSeconds(60);   // default is 60
        executor.setThreadNamePrefix("default-async-executor-");
        // default is new ThreadPoolExecutor.AbortPolicy()
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // ------------------------ END ----------------------------//
        executor.initialize();
        return executor;
    }

    @Bean("defaultCalcExecutor")
    public Executor defaultCalcExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        // ------------------- CONFIGURATION -----------------------//
        executor.setCorePoolSize(availableProcessors + 1);       // default is 1
        executor.setMaxPoolSize(availableProcessors + 1);        // default is Integer.MAX_VALUE
        executor.setQueueCapacity(50);            // default is Integer.MAX_VALUE
        // executor.setKeepAliveSeconds(60);      // default is 60
        executor.setThreadNamePrefix("cpu-calc-async-executor-");
        // default is new ThreadPoolExecutor.AbortPolicy()
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // ------------------------ END ----------------------------//
        executor.initialize();
        return executor;
    }

    @Bean("defaultIOExecutor")
    public Executor defaultIOExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        // ------------------- CONFIGURATION -----------------------//
        executor.setCorePoolSize(availableProcessors + 1);       // default is 1
        executor.setMaxPoolSize(availableProcessors * 2);        // default is Integer.MAX_VALUE
        executor.setQueueCapacity(50);      // default is Integer.MAX_VALUE
        executor.setKeepAliveSeconds(60);   // default is 60
        executor.setThreadNamePrefix("io-async-executor-");
        // default is new ThreadPoolExecutor.AbortPolicy()
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // ------------------------ END ----------------------------//
        executor.initialize();
        return executor;
    }

    @Bean("defaultTaskScheduler")
    public TaskScheduler defaultTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(100);
        scheduler.setThreadNamePrefix("app-scheduler");
        scheduler.initialize();
        return scheduler;
    }
}
