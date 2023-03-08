package com.yan.smarteye.ware.config;


import com.yan.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class MyRabbitMQConfig {


	/**
	 * 消息转换器
	 */
	@Bean
	public MessageConverter messageConverter(){
		return new Jackson2JsonMessageConverter();
	}

	/**
	 * String name, boolean durable 消息是否持久话, boolean autoDelete是否自动删除, Map<String, Object> arguments 其他参数设置
	 */
	//交换机
	@Bean
	public Exchange wareExchange(){
		//广播型	(可以使用一个交换机同时实现普通交换机、死信交换机) （还可以模糊匹配）

		return new TopicExchange(RabbitMQConstant.WARE_EXCHANGE,
				true, false);
	}

	/**
	 * String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments
	 */
	//释放锁定库存的队列（存储死信的队列）
	@Bean
	public Queue wareReleaseQueue(){
		return new Queue(RabbitMQConstant.WARE_RELEASEQUEUE, true,
				false, false);
	}
	// 延时队列
	@Bean
	public Queue wareDelayQueue(){

		Map<String, Object> args = new HashMap<>();
		// 信死了 交给 [ware-exchange] 交换机
		args.put("x-dead-letter-exchange",RabbitMQConstant.WARE_EXCHANGE);
		// 死信路由
		args.put("x-dead-letter-routing-key",RabbitMQConstant.WARE_RELEASEROUTINGKEY);
		args.put("x-message-ttl",900000);

		return new Queue(RabbitMQConstant.WARE_DELAYQUUEUE, true,
				false, false, args);
	}
	//上架onestock增加库存的消息
	@Bean
	public Queue onestockupQueue(){
		return new Queue(RabbitMQConstant.WARE_ONESTOCKUPQUEUE, true,
				false, false);
	}

	/**
	 * 延时队列的绑定关系
	 * String destination, DestinationType destinationType, String exchange, String routingKey, @Nullable Map<String, Object> arguments
	 */
	@Bean
	public Binding wareDelayBinding(){

		return new Binding(RabbitMQConstant.WARE_DELAYQUUEUE, Binding.DestinationType.QUEUE,
				RabbitMQConstant.WARE_EXCHANGE, RabbitMQConstant.WARE_DELAYTOUTINGKEY, null);
	}

	/**
	 * 普通队列（死信队列）的绑定关系
	 * String destination, DestinationType destinationType, String exchange, String routingKey, @Nullable Map<String, Object> arguments
	 */
	@Bean
	public Binding wareReleaseBinding(){

		return new Binding(RabbitMQConstant.WARE_RELEASEQUEUE,Binding.DestinationType.QUEUE,
				RabbitMQConstant.WARE_EXCHANGE,RabbitMQConstant.WARE_RELEASEROUTINGKEY
				, null);
	}

	/**
	 * 上架onestock队列的绑定关系
	 */
	@Bean
	public Binding onestockupBinding(){

		return new Binding(RabbitMQConstant.WARE_ONESTOCKUPQUEUE,Binding.DestinationType.QUEUE,
				RabbitMQConstant.WARE_EXCHANGE,RabbitMQConstant.WARE_ONESTOCKUPROUTINGKEY
				, null);
	}

}
