package cn.xuyk.rabbit.producer.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: Xuyk
 * @Description: 消息记录表实体映射
 * @Date: 2020/6/27
 */
@Data
@Builder
@Table(name = "broker_message")
@AllArgsConstructor
@NoArgsConstructor
public class BrokerMessage implements Serializable {

    private static final long serialVersionUID = 7595620934559360495L;

    /**
     * 消息ID
     */
    @Id
    private String messageId;

    /**
     * 消息体
     */
    private String message;

    /**
     * 重试次数
     */
    private Integer tryCount = 0;

    /**
     * 消息状态(0.发送中 1.发送成功 2.发送失败)
     */
    private String status;

    /**
     * 下次尝试时间点
     */
    private Date nextRetry;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
