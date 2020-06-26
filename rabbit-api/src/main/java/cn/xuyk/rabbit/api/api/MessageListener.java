package cn.xuyk.rabbit.api.api;

import cn.xuyk.rabbit.api.pojo.Message;

/**
 * @Author: Xuyk
 * @Description: 消息监听类
 * @Date: 2020/6/26
 */
public interface MessageListener {

    /**
     * 监听消息
     * @param message
     */
    void onMessage(Message message);

}
