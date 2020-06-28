package cn.xuyk.rabbit.producer.dao;

import cn.xuyk.rabbit.producer.MyMapper;
import cn.xuyk.rabbit.producer.pojo.BrokerMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author: Xuyk
 * @Description: dao层
 * @Date: 2020/6/28
 */
@Repository
public interface BrokerMessageDao extends MyMapper<BrokerMessage> {


    void updateOne(@Param("messageId") String messageId, @Param("message") String message);
}
