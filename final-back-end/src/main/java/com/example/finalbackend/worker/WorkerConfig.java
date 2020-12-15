package com.example.finalbackend.worker;

import com.example.finalbackend.repositories.GlobalsRepository;
import com.example.finalbackend.repositories.UploadRepository;
import com.example.finalbackend.repositories.UserRepository;
import com.example.finalbackend.services.StorageService;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class WorkerConfig {
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(3);
        pool.setMaxPoolSize(3);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }

    @Bean
    @Scope("prototype")
    public ProcessData newProcessData(
            final StorageService service,
            final UserRepository userRepository,
            final UploadRepository uploadRepository,
            final GlobalsRepository globalsRepository) {
        return new ProcessData(service, userRepository, uploadRepository, globalsRepository);
    }
}
