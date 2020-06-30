package cn.xuyk.rabbit.demo.producer;

import cn.xuyk.rabbit.api.common.MessageType;
import cn.xuyk.rabbit.api.pojo.Message;
import cn.xuyk.rabbit.producer.client.ProducerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Xuyk
 * @Description: 以api的形式 + postman 生产消息
 * @Date: 2020/6/30
 */
@RestController
@Slf4j
public class ProducerController {

    @Autowired
    private ProducerClient producerClient;

    @Value("${exchange.name}")
    private String exchangeName;

    /**
     * 消息ID
     */
    private AtomicInteger messageId = new AtomicInteger(100);

    @PostMapping("/produce")
    public String produceMessage(){
        String uuid = String.valueOf(messageId.incrementAndGet());
        Map<String,Object> attributes = new HashMap<>(2);
        attributes.put("name","张三");
        attributes.put("age","18");

        Message message =
                new Message(uuid,exchangeName,"routingKey.demo",attributes,0);
        // 发送可靠性消息
        message.setMessageType(MessageType.RELIANT);
        message.setDelayMills(5000);
        producerClient.send(message);

        return "发送成功";
    }

}
