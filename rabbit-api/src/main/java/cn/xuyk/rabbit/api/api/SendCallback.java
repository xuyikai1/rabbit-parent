package cn.xuyk.rabbit.api.api;

/**
 * @Author: Xuyk
 * @Description: 消息回调函数处理
 * @Date: 2020/6/26
 */
public interface SendCallback {

    /**
     * 发送成功回调
     */
    void onSuccess();

    /**
     * 发送失败回调
     */
    void onFailure();

}
