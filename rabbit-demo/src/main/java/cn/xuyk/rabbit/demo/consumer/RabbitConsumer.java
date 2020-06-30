package cn.xuyk.rabbit.demo.consumer;

import cn.xuyk.rabbit.api.pojo.Message;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: 2020/6/26
 */
@Component
@Slf4j
public class RabbitConsumer {

    /**
     * 配置可以转移到配置文件中而不要在代码里写死,然后使用"${参数名}"的方式
     * durable是否持久化
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${queue.value}",durable = "${queue.durable}"),
            exchange = @Exchange(name = "${exchange.name}",durable = "${exchange.durable}"
                    ,type = "${exchange.type}",ignoreDeclarationExceptions = "${exchange.ignoreDeclarationExceptions}"),
            key = "${key}"))
    @RabbitHandler
    public void onMessage(@Payload Message message,
                          @Headers Map<String,Object> headers,
                          Channel channel) throws Exception{

        // 1.收到消息之后进行业务端消费处理
        log.info("【消费消息】:{}", message);

        // 2.处理成功后,获取deliveryTag并进行手工ack签收
        // 原因:配置文件acknowledge-mode为manual
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        // 手工ack签收 批量写false
        // 重点:如果不签收,则消息会从ready变为uncheck状态,mq会再次发送这条消息
        channel.basicAck(deliveryTag,false);

    }

}
