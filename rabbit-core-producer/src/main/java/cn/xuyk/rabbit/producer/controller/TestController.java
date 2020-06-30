package cn.xuyk.rabbit.producer.controller;

import cn.xuyk.rabbit.producer.constant.BrokerMessageStatus;
import cn.xuyk.rabbit.producer.dao.BrokerMessageDao;
import cn.xuyk.rabbit.producer.pojo.BrokerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

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
    public void test(@RequestParam String message,
                     @RequestParam String messageId){
        brokerMessageDao.insert(
                BrokerMessage.builder()
                .messageId(messageId)
                .message(message)
                .tryCount(3)
                .createTime(new Date())
                .updateTime(new Date())
                .build()
        );
        Example example = new Example(BrokerMessage.class);
        example.createCriteria()
                .andEqualTo("status", BrokerMessageStatus.SENDING.getStatus())
                .andLessThan("nextRetry",new Date());
        List<BrokerMessage> list = brokerMessageDao.selectByExample(example);
        System.out.println(list);
    }

}
