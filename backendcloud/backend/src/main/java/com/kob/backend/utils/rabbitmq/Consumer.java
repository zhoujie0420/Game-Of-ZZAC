package com.kob.backend.utils.rabbitmq;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queuesToDeclare = @Queue(value = "queue", durable = "false", autoDelete = "true"))
public class Consumer {
    /**
     * @param message 参数message为消息的内容
     */
    @RabbitHandler
    public void consume(String message) {
        System.out.println("rabbitmq" + message);
    }

}
