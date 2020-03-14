package com.dubbo.example.consumer.controller;

import com.dubbo.example.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${dubbo.application.name}")
    private String serviceName;

    @Reference
    private DemoService demoService;

    @RequestMapping("/")
    public String index(){
        logger.info("****************{}*******************",serviceName);
        return demoService.sayHello(serviceName);
    }
}
