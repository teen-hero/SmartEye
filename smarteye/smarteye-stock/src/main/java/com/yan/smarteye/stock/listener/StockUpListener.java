package com.yan.smarteye.stock.listener;

import com.rabbitmq.client.Channel;
import com.yan.common.constant.RabbitMQConstant;
import com.yan.smarteye.stock.entity.OnestockEntity;
import com.yan.smarteye.stock.service.StockService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
  监听整批上架尝试消息
 */
@Service									//autoStartup = "false"关闭自动启动  id = "stockuplistener"添加id属性
@RabbitListener(queues = RabbitMQConstant.STOCK_UPSTOCKQUEUE,autoStartup = "false",id = "stockuplistener")
public class StockUpListener {
	@Autowired
	StockService stockService;

	/**
	 *  整批上架尝试
	 */
	@RabbitHandler
	public void upHandle(OnestockEntity onestockEntity,
						 Message message,
						 Channel channel) throws IOException {
		try {
			stockService.up(onestockEntity.getStockId());
			// 执行成功的 签收
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			//解锁库存失败、异常记得拒收
			channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
		}
	}

}
