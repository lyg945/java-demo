package com.dubbo.example.consumer.controller;

import com.dubbo.example.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${dubbo.application.name}")
    private String serviceName;

    private String springTopic = "string-topic:tagA";


    @Reference
    private DemoService demoService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/")
    public String index(){
        logger.info("****************{}*******************",serviceName);
        // Send string
        SendResult sendResult = rocketMQTemplate.syncSend(springTopic, "Hello, rocketmq!");
        System.out.printf("syncSend1 to topic %s sendResult=%s %n", springTopic, sendResult);

        return demoService.sayHello(serviceName);
    }
}
