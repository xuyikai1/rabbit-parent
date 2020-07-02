package cn.xuyk.rabbit.demo.consumer;

import cn.xuyk.rabbit.api.pojo.Message;
import cn.xuyk.rabbit.common.convert.GenericMessageConverter;
import cn.xuyk.rabbit.common.convert.RabbitMessageConverter;
import cn.xuyk.rabbit.common.serializer.Serializer;
import cn.xuyk.rabbit.common.serializer.impl.JacksonSerializer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Xuyk
 * @Description: 初始化序列化器bean
 * @Date: 2020/7/2
 */
@Configuration
public class ConvertConfig {

    @Bean
    public MessageConverter messageConverter() {
        // 消费者新增Jackson序列化器
        Serializer serializer = JacksonSerializer.createParametricType(Message.class);
        GenericMessageConverter gmc = new GenericMessageConverter(serializer);
        // 装饰者模式
        return new RabbitMessageConverter(gmc);
    }

}
