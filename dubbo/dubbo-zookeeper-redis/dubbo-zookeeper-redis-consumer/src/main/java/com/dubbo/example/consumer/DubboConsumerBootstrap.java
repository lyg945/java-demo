package com.dubbo.example.consumer;
import com.dubbo.example.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@SpringBootApplication
public class DubboConsumerBootstrap {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference
    private DemoService demoService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    private String springTopic = "string-topic:tagA";

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerBootstrap.class);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            logger.info(demoService.sayHello("*************hello dubbo*************"));

            // Send string
            SendResult sendResult = rocketMQTemplate.syncSend(springTopic, "Hello, rocketmq!");
            System.out.printf("syncSend1 to topic %s sendResult=%s %n", springTopic, sendResult);
        };
    }
}
