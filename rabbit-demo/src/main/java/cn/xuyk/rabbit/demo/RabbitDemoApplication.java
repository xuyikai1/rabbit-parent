package cn.xuyk.rabbit.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: Xuyk
 * @Description: 启动类
 * @Date: 2020/6/30
 */
@MapperScan(basePackages = "cn.xuyk.rabbit.**.dao")
@SpringBootApplication
public class RabbitDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitDemoApplication.class, args);
    }

}
