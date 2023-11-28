package io.github.okafke.aitcg.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class TestConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        return new SyncTaskExecutor();
    }

}
