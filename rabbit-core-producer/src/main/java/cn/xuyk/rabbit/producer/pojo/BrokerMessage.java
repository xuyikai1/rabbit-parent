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

    @Id
    private String messageId;

    private String message;

    private Integer tryCount = 0;

    private String status;

    private Date nextRetry;

    private Date createTime;

    private Date updateTime;

}
