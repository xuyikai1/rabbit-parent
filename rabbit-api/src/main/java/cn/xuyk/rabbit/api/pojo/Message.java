package cn.xuyk.rabbit.api.pojo;

import cn.xuyk.rabbit.api.common.MessageType;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Xuyk
 * @Description: 消息类
 * @Date: 2020/6/26
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = -5171284810277840403L;

    /**
     * 唯一的消息ID
     */
    private String messageId;

    /**
     * 消息的主题
     */
    private String topic;

    /**
     * 消息的路由规则
     */
    private String routingKey = "";

    /**
     * 消息的附加属性
     */
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * 延迟消息的参数配置,消息的延迟时间 默认10秒
     */
    private int delayMills = 10000;

    /**
     * 消息类型：默认为confirm消息类型
     */
    private String messageType = MessageType.CONFIRM;

    public Message() {
    }

    public Message(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMills) {
        this.messageId = messageId;
        this.topic = topic;
        this.routingKey = routingKey;
        this.attributes = attributes;
        this.delayMills = delayMills;
    }

    public Message(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMills,
                   String messageType) {
        this.messageId = messageId;
        this.topic = topic;
        this.routingKey = routingKey;
        this.attributes = attributes;
        this.delayMills = delayMills;
        this.messageType = messageType;
    }
}
