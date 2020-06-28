package cn.xuyk.rabbit.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: Xuyk
 * @Description:
 * @Date: 2020/6/28
 */
@MapperScan(basePackages = "cn.xuyk.rabbit.**.dao")
@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

}
