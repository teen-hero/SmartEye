package com.yan.smarteye.stock.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 配置线程池
 */
@EnableConfigurationProperties(com.yan.smarteye.stock.config.ThreadPoolConfigProperties.class)   //开启属性配置
@Configuration
public class MyThreadConfig {

	//向容器中放一个线程池
	//（搭配ThreadPoolConfigProperties，实现在配置文件中配置这个线程池的参数）
	@Bean
	public ThreadPoolExecutor threadPoolExecutor(com.yan.smarteye.stock.config.ThreadPoolConfigProperties threadPoolConfigProperties){
		return new ThreadPoolExecutor(threadPoolConfigProperties.getCoreSize(),
				threadPoolConfigProperties.getMaxSize(),
				threadPoolConfigProperties.getKeepAliveTime() ,TimeUnit.SECONDS,
				new LinkedBlockingDeque<>(1000), Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.AbortPolicy());
	}
}
