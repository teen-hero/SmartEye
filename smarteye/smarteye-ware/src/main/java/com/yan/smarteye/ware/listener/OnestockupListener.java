package com.yan.smarteye.ware.listener;

import com.rabbitmq.client.Channel;
import com.yan.common.constant.RabbitMQConstant;
import com.yan.smarteye.ware.entity.OnestockupTaskEntity;
import com.yan.smarteye.ware.entity.OutbillTaskEntity;
import com.yan.smarteye.ware.service.WarestockService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 监听上架oenstock消息，有错误的话根据消息回滚库存
 */
@Service
@RabbitListener(queues = RabbitMQConstant.WARE_ONESTOCKUPQUEUE)
public class OnestockupListener {

	@Autowired
	WarestockService warestockService;

	/**
	 * 监听上架oenstock消息
	 */
	@RabbitHandler
	public void onestockupHandle(OnestockupTaskEntity onestockupTaskEntity,
								 Message message,
								 Channel channel) throws IOException {
		try {
			warestockService.downStock(onestockupTaskEntity);
			// 执行成功的 签收
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			//解锁库存失败、异常记得拒收
			channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
		}
	}

}
