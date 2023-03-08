package com.yan.smarteye.stock.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "smarteye.thread")
//@Component，因为线程池配置类中有@EnableConfigurationProperties注解，就不用再加@Component了
@Data
public class ThreadPoolConfigProperties {

	//让以下三个线程池参数在配置文件中可配置

	private Integer coreSize;

	private Integer maxSize;

	private Integer keepAliveTime;
}
