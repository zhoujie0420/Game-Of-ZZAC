package com.kob.matchingsystem;

import com.kob.matchingsystem.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MatchSystemApplication {

    public static void main(String[] args) {
        MatchingServiceImpl.matchingPool.start();   // 启动匹配线程
        SpringApplication.run(MatchSystemApplication.class, args);
    }
}
