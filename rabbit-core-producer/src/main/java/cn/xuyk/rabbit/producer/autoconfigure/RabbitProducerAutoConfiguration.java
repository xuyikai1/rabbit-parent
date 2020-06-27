package cn.xuyk.rabbit.producer.autoconfigure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Xuyk
 * @Description: 自动装配类
 * @Date: 2020/6/27
 */
@Configuration
@ComponentScan({"cn.xuyk.rabbit.producer.*"})
public class RabbitProducerAutoConfiguration {
}
