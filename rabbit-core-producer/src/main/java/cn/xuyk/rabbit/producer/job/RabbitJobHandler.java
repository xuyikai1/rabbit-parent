package cn.xuyk.rabbit.producer.job;

import cn.hutool.json.JSONUtil;
import cn.xuyk.rabbit.api.pojo.Message;
import cn.xuyk.rabbit.producer.broker.RabbitBroker;
import cn.xuyk.rabbit.producer.constant.BrokerMessageStatus;
import cn.xuyk.rabbit.producer.dao.BrokerMessageDao;
import cn.xuyk.rabbit.producer.pojo.BrokerMessage;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

import static com.xxl.job.core.handler.IJobHandler.SUCCESS;

/**
 * @Author: Xuyk
 * @Description: xxl-job定时任务
 * @Date: 2020/6/30
 */
@Component
@Slf4j
public class RabbitJobHandler {

    @Autowired
    private RabbitBroker rabbitBroker;
    @Autowired
    private BrokerMessageDao brokerMessageDao;

    private static final int MAX_RETRY_COUNT = 3;

    @XxlJob("RabbitJobHandler")
    public ReturnT<String> rabbitJobHandler(String param) throws Exception {
        XxlJobLogger.log("XXL-JOB, Hello World.");

        Example example = new Example(BrokerMessage.class);
        example.createCriteria()
                .andEqualTo("status", BrokerMessageStatus.SENDING.getStatus())
                .andLessThan("nextRetry",new Date());
        List<BrokerMessage> brokerMessages = brokerMessageDao.selectByExample(example);

        XxlJobLogger.log("【CheckMessageList】：{}",brokerMessages);

        Date now = new Date();
        brokerMessages.forEach( brokerMessage -> {

            String messageId = brokerMessage.getMessageId();

            // 重试次数超过最大重试次数则消息发送失败
            if(brokerMessage.getTryCount() >= MAX_RETRY_COUNT) {
                XxlJobLogger.log("【This is Max retry count】：{}",brokerMessage);
                BrokerMessage bm = BrokerMessage.builder()
                        .messageId(messageId)
                        .status(BrokerMessageStatus.SEND_FAIL.getStatus())
                        .updateTime(now).build();
                brokerMessageDao.updateByPrimaryKeySelective(bm);
                log.warn(" -----消息设置为最终失败，消息ID: {} -------", messageId);
            } else {
                XxlJobLogger.log("【ReSend Message】：{}",brokerMessage);

                //	每次重发的时候要更新一下try count字段
                brokerMessageDao.update4TryCount(messageId,now);
                // 	重发消息
                String message = brokerMessage.getMessage();
                rabbitBroker.reliantSend(JSONUtil.toBean(message,Message.class));
            }

        });

        return SUCCESS;
    }

}
