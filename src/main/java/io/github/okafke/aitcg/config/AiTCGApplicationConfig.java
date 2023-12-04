package io.github.okafke.aitcg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class AiTCGApplicationConfig {
}
