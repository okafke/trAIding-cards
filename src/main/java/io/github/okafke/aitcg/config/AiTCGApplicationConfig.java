package io.github.okafke.aitcg.config;

import io.github.okafke.aitcg.cli.CommandExceptionResolverImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class AiTCGApplicationConfig {
    @Bean
    public CommandExceptionResolverImpl customExceptionResolver() {
        return new CommandExceptionResolverImpl();
    }

}
