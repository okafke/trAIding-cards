package io.github.okafke.aitcg.config;

import io.github.okafke.aitcg.cli.CommandExceptionResolverImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class AiTCGApplicationConfig {
    @Bean
    public CommandExceptionResolverImpl customExceptionResolver() {
        return new CommandExceptionResolverImpl();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurerAdapter() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/cards/**").addResourceLocations("file:cards/");
            }
        };
    }

}
