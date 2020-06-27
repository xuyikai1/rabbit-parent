package cn.xuyk.rabbit.producer.broker.impl;

import cn.xuyk.rabbit.api.common.MessageType;
import cn.xuyk.rabbit.api.pojo.Message;
import cn.xuyk.rabbit.producer.broker.RabbitBroker;
import cn.xuyk.rabbit.producer.broker.container.RabbitTemplateContainer;
import cn.xuyk.rabbit.producer.broker.queue.AsyncBaseQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Xuyk
 * @Description: 真正的发送不同类型的消息实现类
 * @Date: 2020/6/27
 */
@Component
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {

    @Autowired
    private RabbitTemplateContainer rabbitTemplateContainer;
    @Autowired
    private AsyncBaseQueue asyncBaseQueue;

    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    /**
     * 	发送消息的核心方法 使用异步线程池进行发送消息
     * @param message
     */
    private void sendKernel(Message message) {
        asyncBaseQueue.submit(() -> {
            // %s代表一个占位符 %s#%s#%s -> 消息ID#当前时间#消息类型
            CorrelationData correlationData =
                    new CorrelationData(String.format("%s#%s#%s",
                            message.getMessageId(),
                            System.currentTimeMillis(),
                            message.getMessageType()));
            String topic = message.getTopic();
            String routingKey = message.getRoutingKey();
            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq, messageId: {}", message.getMessageId());
        });
    }


    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {

    }

    @Override
    public void sendMessages() {

    }

}
