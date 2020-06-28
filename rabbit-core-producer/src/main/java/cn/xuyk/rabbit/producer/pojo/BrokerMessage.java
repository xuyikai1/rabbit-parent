package cn.xuyk.rabbit.producer.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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

    @Column(name = "message")
    private String message;

    @Column(name = "try_count")
    private Integer tryCount = 0;

    @Column(name = "status")
    private String status;

    @Column(name = "next_retry")
    private Date nextRetry;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

}
