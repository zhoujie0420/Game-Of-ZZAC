package com.kob.backend.config;


//配置RestTemplate
//向MatchingSystem发请求，需要借助Springboot中的一个工具RestTemplate，它可以在两个Spring进程之间进行通信。
//
//先配置一下这个工具，如果希望在WebSocketServer.java中使用RestTemplate，就需要加Bean注解，这样才能够取出来。
//
//可以理解为，需要用到某个工具的时候，就定义一个它的Configuration，加一个注解Bean，返回一个它的实例。

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
