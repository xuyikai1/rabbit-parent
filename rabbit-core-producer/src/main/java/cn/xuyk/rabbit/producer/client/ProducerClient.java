package cn.xuyk.rabbit.producer.client;

import cn.xuyk.rabbit.api.api.MessageProducer;
import cn.xuyk.rabbit.api.api.SendCallback;
import cn.xuyk.rabbit.api.common.MessageType;
import cn.xuyk.rabbit.api.exception.MessageRunTimeException;
import cn.xuyk.rabbit.api.pojo.Message;
import cn.xuyk.rabbit.producer.broker.MessageHolder;
import cn.xuyk.rabbit.producer.broker.RabbitBroker;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Xuyk
 * @Description: 生产消息实现类
 * @Date: 2020/6/27
 */
@Component
public class ProducerClient implements MessageProducer {

    @Autowired
    private RabbitBroker rabbitBroker;

    @Override
    public void send(Message message) throws MessageRunTimeException {
        // 校验topic
        Preconditions.checkNotNull(message.getTopic());
        // 根据消息类型采用不同的策略
        String messageType = message.getMessageType();
        switch (messageType) {
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case MessageType.RELIANT:
                rabbitBroker.reliantSend(message);
                break;
            default:
                break;
        }
    }

    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {
        messages.forEach( message -> {
            // 传输迅速消息
            message.setMessageType(MessageType.RAPID);
            MessageHolder.add(message);
        });
        rabbitBroker.sendMessages();
    }

    @Override
    public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {

    }

}
