package com.yan.smarteye.stock.config;

import com.yan.common.constant.RabbitMQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 配置MQ，尝试新的配置方式
 */
@Slf4j
@Configuration
public class MyRabbitMQConfig {

	private RabbitTemplate rabbitTemplate;

	@Primary  //优先注入我们配置的 RabbitTemplate
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		this.rabbitTemplate = rabbitTemplate;
		//配置序列化方式
		rabbitTemplate.setMessageConverter(messageConverter());
		// 设置回调
		initRabbitTemplate();
		return rabbitTemplate;
	}

	@Bean
	public MessageConverter messageConverter(){
		return new Jackson2JsonMessageConverter();
	}

	//源码没有自动注入这个组件，我们手动注入一个，后面操作监听器的手动开启、关闭要用
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
		RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry = new RabbitListenerEndpointRegistry();
		return rabbitListenerEndpointRegistry;
	}

	public void initRabbitTemplate(){
		/**
		 * 	设置确认回调  消息到达broker
		 *  correlationData: 消息的唯一id
		 *  ack： 消息是否成功收到
		 * 	cause：失败的原因
		 */
		rabbitTemplate.setConfirmCallback(
				(correlationData, ack , cause) ->
						log.info("\nbroker收到消息: " + correlationData + "\tack: " + ack + "\tcause： " + cause));

		/**
		 * 只要消息没有投递给指定的队列，就触发这个失败回调
		 *
		 * message: 投递失败的消息详细信息
		 * replyCode: 回复的状态码
		 * replyText: 回复的文本内容
		 * exchange: 当时这个发送给那个交换机
		 * routerKey: 当时这个消息用那个路由键
		 */
		rabbitTemplate.setReturnCallback(
				(message, replyCode, replyText, exchange, routerKey)
						-> log.error("Fail Message [" + message + "]" + "\treplyCode: " + replyCode + "\treplyText:" + replyText + "\texchange:" + exchange + "\trouterKey:" + routerKey));
	}

	//交换机
	@Bean
	public Exchange stockExchange(){
		//点对点
		return new DirectExchange(RabbitMQConstant.STOCK_EXCHANGE,
				true, false);
	}


	//尝试上架stock的消息队列
	@Bean
	public Queue upStockQueue(){
		return new Queue(RabbitMQConstant.STOCK_UPSTOCKQUEUE, true,
				false, false);
	}


	/**
	 * 绑定关系
	 */
	@Bean
	public Binding upStockBinding(){

		return new Binding(RabbitMQConstant.STOCK_UPSTOCKQUEUE,Binding.DestinationType.QUEUE,
				RabbitMQConstant.STOCK_EXCHANGE,RabbitMQConstant.STOCK_UPSTOCKROUTINGKEY
				, null);
	}

}
