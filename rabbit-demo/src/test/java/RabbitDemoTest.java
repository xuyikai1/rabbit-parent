import cn.xuyk.rabbit.api.common.MessageType;
import cn.xuyk.rabbit.api.pojo.Message;
import cn.xuyk.rabbit.demo.RabbitDemoApplication;
import cn.xuyk.rabbit.producer.client.ProducerClient;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: 2020/6/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitDemoApplication.class)
public class RabbitDemoTest {

    @Autowired
    private ProducerClient producerClient;

    @Test
    public void test() throws InterruptedException {

        String uuid = UUID.randomUUID().toString();
        Map<String,Object> attributes = new HashMap<>();
        attributes.put("name","张三");
        attributes.put("age","18");

        Message message =
                new Message(uuid,"exchange-1","routingKey.demo",attributes,0);
        // 发送可靠性消息
        message.setMessageType(MessageType.RELIANT);
        message.setDelayMills(5000);
        producerClient.send(message);

        Thread.sleep(1000000);
    }

}
