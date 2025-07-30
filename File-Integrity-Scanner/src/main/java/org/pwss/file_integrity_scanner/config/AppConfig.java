package org.pwss.file_integrity_scanner.config;



import org.jspecify.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AppConfig implements AsyncConfigurer {


    // Thread pool config values
    private final int corePoolSize = 5;
    private final int maxPoolSize = 10;
    private final int queueCapacity = 25;

    //Log

    private final org.slf4j.Logger log = LoggerFactory.getLogger(AppConfig.class);


    /**
     * Handeling exceptions in asynchronous methods
     */
    @Override
    public @Nullable AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, obj) -> {
            log.error("Exception in method: {}",method.getName());
            throwable.printStackTrace();
        };
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
