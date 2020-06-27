package cn.xuyk.rabbit.producer.broker.queue;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Xuyk
 * @Description: 线程池参数
 * @Date: 2020/6/27
 */
@Configuration
@ConfigurationProperties(prefix = "thread.pool")
@Data
public class ThreadPool {

    /**
     * 线程池名称
     */
    private String threadPoolName;
    /**
     * 线程池核心线程数
     * 1.cpu密集型 cpu个数+1
     * 2.io密集型  cpu个数*2
     */
    private Integer coreThreadSize;

    /**
     * 线程池最大线程数
     */
    private Integer maxThreadSize;

    /**
     * 线程池中空闲线程等待工作的超时时间
     */
    private Long keepAliveTime;

    /**
     * 线程池容量
     */
    private Integer capacity;

}
