package com.yan.smarteye.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableCaching
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class SmartEyeStockApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartEyeStockApplication.class);
    }
}
