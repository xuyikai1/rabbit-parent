package cn.xuyk.rabbit.producer.constant;

/**
 * @Author: Xuyk
 * @Description: 消息的发送状态
 * @Date: 2020/6/29
 */
public enum BrokerMessageStatus {

    /**
     * 发送中
     */
    SENDING("0"),
    /**
     * 发送成功
     */
    SEND_OK("1"),
    /**
     * 发送失败
     */
    SEND_FAIL("2");

    private final String status;

    BrokerMessageStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
