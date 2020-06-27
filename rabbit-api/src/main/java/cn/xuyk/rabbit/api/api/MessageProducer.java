package cn.xuyk.rabbit.api.api;

import cn.xuyk.rabbit.api.exception.MessageRunTimeException;
import cn.xuyk.rabbit.api.pojo.Message;

import java.util.List;

/**
 * @Author: Xuyk
 * @Description: 消息生产者
 * @Date: 2020/6/26
 */
public interface MessageProducer {

    /**
     * 发送消息
     * @param message
     * @throws MessageRunTimeException
     */
    void send(Message message) throws MessageRunTimeException;

    /**
     * 批量发送消息
     * @param messages
     * @throws MessageRunTimeException
     */
    void send(List<Message> messages) throws MessageRunTimeException;

    /**
     * 发送消息 附带SendCallback回调执行响应的业务逻辑处理
     * @param message
     * @param sendCallback
     * @throws MessageRunTimeException
     */
    void send(Message message, SendCallback sendCallback) throws MessageRunTimeException;

}
