package io.github.okafke.aitcg.test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.shell.jline.InteractiveShellRunner;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
@TestConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
public class TestConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        return new SyncTaskExecutor();
    }

}
