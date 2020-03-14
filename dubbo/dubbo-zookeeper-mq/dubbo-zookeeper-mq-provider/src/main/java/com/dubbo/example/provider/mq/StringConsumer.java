package com.dubbo.example.provider.mq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "string-topic", consumerGroup = "string_consumer", selectorExpression = "*")
public class StringConsumer implements RocketMQListener<String> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(String message) {
        logger.info("------- StringConsumer received: {} \n", message);
    }
}