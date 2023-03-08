package com.yan.smarteye.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.HashMap;
import java.util.HashSet;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})  //因为引入了数据源依赖，我们又没配置数据源（该微服务不需要），所有要排除加载数据源，否则报错
@EnableDiscoveryClient
public class SmartEyeGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartEyeGatewayApplication.class);

    }
}
