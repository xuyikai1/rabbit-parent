package cn.xuyk.rabbit.api.exception;

/**
 * @Author: Xuyk
 * @Description: 消息运行时异常类
 * @Date: 2020/6/26
 */
public class MessageRunTimeException extends RuntimeException {

    private static final long serialVersionUID = -8304412475983955630L;

    public MessageRunTimeException() {
        super();
    }

    public MessageRunTimeException(String message) {
        super(message);
    }

    public MessageRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageRunTimeException(Throwable cause) {
        super(cause);
    }

}
