package cn.xuyk.rabbit.producer.broker;

import cn.xuyk.rabbit.api.pojo.Message;

/**
 * @Author: Xuyk
 * @Description: 具体发送不同种类型消息的接口
 * @Date: 2020/6/27
 */
public interface RabbitBroker {

    /**
     * 发送迅速消息
     * @param message
     */
    void rapidSend(Message message);

    /**
     * 发送确认消息
     * @param message
     */
    void confirmSend(Message message);

    /**
     * 发送可靠性消息
     * @param message
     */
    void reliantSend(Message message);

    /**
     * 批量发送消息
     */
    void sendMessages();

}
