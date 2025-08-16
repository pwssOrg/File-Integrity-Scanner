package org.pwss.file_integrity_scanner.config;


import org.jspecify.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableAsync
@EnableScheduling
@Configuration
public class AppConfig implements AsyncConfigurer {


    // Thread pool config values
    private final int corePoolSize = 10;
    private final int maxPoolSize = 50;
    private final int queueCapacity = 200;

    //Log

    private final org.slf4j.Logger log = LoggerFactory.getLogger(AppConfig.class);


    /**
     * Handeling exceptions in asynchronous methods
     */
    @Override
    public @Nullable AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, obj) -> {
            log.error("Exception in method: {}", method.getName());
            throwable.printStackTrace();
        };
    }

    /**
     * Creates and configures a TaskScheduler bean.
     * <p>
     * This method provides a ThreadPoolTaskScheduler instance, which is used
     * for scheduling tasks in a multithreaded environment.
     *
     * @return a configured instance of ThreadPoolTaskScheduler
     */
    @Bean(name = "threadPoolTaskScheduler")
    public TaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("MySchedulerThread-");
        scheduler.setRejectedExecutionHandler((r, executor) -> log.warn("Scheduling rejected: task={}, executor={}", r, executor));
        return scheduler;
    }

    /**
     * Here is the Spring App Exectuor
     */

    @Primary
    @Bean(name = "taskExecutorDefault")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("MyAsyncThread-");
        executor.setRejectedExecutionHandler((r, executor1) -> log.warn("Task rejected, thread pool is full and queue is also full"));
        executor.initialize();
        return executor;
    }
}
