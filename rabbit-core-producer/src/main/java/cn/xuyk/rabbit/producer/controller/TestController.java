package cn.xuyk.rabbit.producer.controller;

import cn.xuyk.rabbit.producer.dao.BrokerMessageDao;
import cn.xuyk.rabbit.producer.pojo.BrokerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author: Xuyk
 * @Description: 测试接口
 * @Date: 2020/6/28
 */
@RestController
public class TestController {

    @Autowired
    private BrokerMessageDao brokerMessageDao;

    @PostMapping("/test")
    public void test(@RequestParam String message){
        brokerMessageDao.insert(
                BrokerMessage.builder()
                .messageId("123")
                .message("xxx")
                .tryCount(3)
                .createTime(new Date())
                .updateTime(new Date())
                .build()
        );
        brokerMessageDao.updateOne("1232",message);
    }

}
