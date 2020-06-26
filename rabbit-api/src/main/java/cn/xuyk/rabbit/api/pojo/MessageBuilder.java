package cn.xuyk.rabbit.api.pojo;

import cn.xuyk.rabbit.api.exception.MessageRunTimeException;
import cn.xuyk.rabbit.api.common.MessageType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Xuyk
 * @Description: 建造者模式
 * 特点：
 *      1.原封不动地复制需要构造的pojo类的属性
 *      2.构造函数私有化 private
 *      3.对外提供create和build等静态方法
 *      4.提供链式变成方法 - withAttribute()等方法
 *      5.可以在build方法中增加一些当前类相关的校验，例如id不能为空或者为空时自动创建一个id等
 * @Date: 2020/6/26
 */
public class MessageBuilder {

    private String messageId;
    private String topic;
    private String routingKey = "";
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private int delayMills;
    private String messageType = MessageType.CONFIRM;

    private MessageBuilder() {
    }

    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    public MessageBuilder withMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public MessageBuilder withTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder withRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public MessageBuilder withAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }

    public MessageBuilder withAttribute(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    public MessageBuilder withDelayMills(int delayMills) {
        this.delayMills = delayMills;
        return this;
    }

    public MessageBuilder withMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public Message build() {

        // 1.校验messageID
        if(messageId == null) {
            messageId = UUID.randomUUID().toString();
        }
        // 2.校验topic
        if(topic == null) {
            throw new MessageRunTimeException("this topic is null");
        }
        return new Message(messageId, topic, routingKey, attributes, delayMills, messageType);
    }

}
