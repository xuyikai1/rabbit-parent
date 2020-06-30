package cn.xuyk.rabbit.producer.dao;

import cn.xuyk.rabbit.producer.MyMapper;
import cn.xuyk.rabbit.producer.pojo.BrokerMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author: Xuyk
 * @Description: dao层
 * @Date: 2020/6/28
 */
@Mapper
public interface BrokerMessageDao extends MyMapper<BrokerMessage> {

    /**
     * 查询超时的消息列表
     * @param status
     * @return
     */
    List<BrokerMessage> queryBrokerMessageStatus4Timeout(@Param("status")String status);

    /**
     * 更新重试次数
     * @param brokerMessageId
     * @param updateTime
     * @return
     */
    int update4TryCount(@Param("brokerMessageId")String brokerMessageId, @Param("updateTime")Date updateTime);

}
