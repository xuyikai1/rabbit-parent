package cn.xuyk.rabbit.common.convert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * @Author: Xuyk
 * @Description: 装饰 序列化转换器：在properties中
 * @Date: 2020/6/27
 */
public class RabbitMessageConverter implements MessageConverter {

    private GenericMessageConverter delegate;

    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        Preconditions.checkNotNull(genericMessageConverter);
        this.delegate = genericMessageConverter;
    }

    /**
     * cn.xuyk.rabbit.api.pojo.Message -> org.springframework.amqp.core.Message
     * @param object
     * @param messageProperties 可以在properties里增加一些附加属性set进
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        cn.xuyk.rabbit.api.pojo.Message message = (cn.xuyk.rabbit.api.pojo.Message)object;
        messageProperties.setDelay(message.getDelayMills());
        return this.delegate.toMessage(object, messageProperties);
    }

    /**
     * org.springframework.amqp.core.Message -> cn.xuyk.rabbit.api.pojo.Message
     * @param message
     * @return
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        cn.xuyk.rabbit.api.pojo.Message msg = (cn.xuyk.rabbit.api.pojo.Message) this.delegate.fromMessage(message);
        return msg;
    }

}
