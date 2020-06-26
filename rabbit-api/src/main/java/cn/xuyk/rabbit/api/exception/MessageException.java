package cn.xuyk.rabbit.api.exception;

/**
 * @Author: Xuyk
 * @Description: 消息异常类
 * @Date: 2020/6/26
 */
public class MessageException extends Exception {

    private static final long serialVersionUID = -3793852268630770174L;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

}
