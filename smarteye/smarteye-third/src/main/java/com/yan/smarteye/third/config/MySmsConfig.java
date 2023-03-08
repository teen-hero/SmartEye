package com.yan.smarteye.third.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Configuration
public class MySmsConfig {
    //使这两个属性可配置
    private String accessKeyId;
    private String accessKeySecret;

    //向IOC中放入一个阿里云短信服务的连接，后续只拿这一个认证好的连接就行了，就不用一个个新建连接了
    @Bean
    public com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

}
