package com.yan.smarteye.third;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SmartEyeThirdApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartEyeThirdApplication.class,args);
    }
}
