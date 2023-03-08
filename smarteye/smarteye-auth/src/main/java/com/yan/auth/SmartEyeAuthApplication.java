package com.yan.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession //开启SpringSession，创建了一个springSessionRepositoryFilter,负责将原生的HttpSession 替换为SpringSession的实现
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class SmartEyeAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartEyeAuthApplication.class,args);
    }
}
