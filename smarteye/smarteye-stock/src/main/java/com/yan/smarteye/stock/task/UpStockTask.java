package com.yan.smarteye.stock.task;


import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UpStockTask {
    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    //每天22点执行一次该方法(晚上10点开始)
    //开启监听器，消费队列消息
    @Scheduled(cron = "0 0 22 * * ?")
    public void upStock(){
        MessageListenerContainer stockuplistener = rabbitListenerEndpointRegistry.getListenerContainer("stockuplistener");
        if(!stockuplistener.isRunning()){
            stockuplistener.start();
        }
    }

    //每天5点执行一次该方法(早上5点开始)
    //关闭监听器，停止消费队列消息
    @Scheduled(cron = "0 0 5 * * ?")
    public void stopUpStock(){
        MessageListenerContainer stockuplistener = rabbitListenerEndpointRegistry.getListenerContainer("stockuplistener");
        if(stockuplistener.isRunning()){
            stockuplistener.stop();
        }
    }
}
