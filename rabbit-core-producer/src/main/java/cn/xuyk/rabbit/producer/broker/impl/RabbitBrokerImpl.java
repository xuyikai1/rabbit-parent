package cn.xuyk.rabbit.producer.broker.impl;

import cn.hutool.json.JSONUtil;
import cn.xuyk.rabbit.api.common.MessageType;
import cn.xuyk.rabbit.api.pojo.Message;
import cn.xuyk.rabbit.producer.broker.MessageHolder;
import cn.xuyk.rabbit.producer.broker.RabbitBroker;
import cn.xuyk.rabbit.producer.broker.container.RabbitTemplateContainer;
import cn.xuyk.rabbit.producer.broker.queue.AsyncBaseQueue;
import cn.xuyk.rabbit.producer.broker.queue.MessageHolderAsyncBaseQueue;
import cn.xuyk.rabbit.producer.constant.BrokerMessageConst;
import cn.xuyk.rabbit.producer.constant.BrokerMessageStatus;
import cn.xuyk.rabbit.producer.dao.BrokerMessageDao;
import cn.xuyk.rabbit.producer.pojo.BrokerMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

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
    @Autowired
    private MessageHolderAsyncBaseQueue messageHolderAsyncBaseQueue;
    @Autowired
    private BrokerMessageDao brokerMessageDao;

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
        message.setMessageType(MessageType.RELIANT);
        BrokerMessage bm = brokerMessageDao.selectByPrimaryKey(message.getMessageId());
        if(bm == null) {
            // 1.把数据库的消息发送日志先记录好
            Date now = new Date();
            // tryCount 在最开始发送的时候不需要进行设置
            BrokerMessage brokerMessage = new BrokerMessage();
            brokerMessage.setMessageId(message.getMessageId());
            brokerMessage.setStatus(BrokerMessageStatus.SENDING.getStatus());
            // nextRetry设置为1分钟,一分钟之后check status(定时任务扫描nextRetry时间到的数据)
            brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
            brokerMessage.setCreateTime(now);
            brokerMessage.setUpdateTime(now);
            brokerMessage.setMessage(JSONUtil.toJsonStr(message));
            brokerMessageDao.insertSelective(brokerMessage);
        }
        //2. 执行真正的发送消息逻辑
        sendKernel(message);
    }

    @Override
    public void sendMessages() {
        List<Message> messages = MessageHolder.getAndClear();
        messages.forEach(message -> {
            messageHolderAsyncBaseQueue.submit(() -> {
                CorrelationData correlationData =
                        new CorrelationData(String.format("%s#%s#%s",
                                message.getMessageId(),
                                System.currentTimeMillis(),
                                message.getMessageType()));
                String topic = message.getTopic();
                String routingKey = message.getRoutingKey();
                RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
                rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
                log.info("#RabbitBrokerImpl.sendMessages# send to rabbitmq, messageId: {}", message.getMessageId());
            });
        });
    }

}
