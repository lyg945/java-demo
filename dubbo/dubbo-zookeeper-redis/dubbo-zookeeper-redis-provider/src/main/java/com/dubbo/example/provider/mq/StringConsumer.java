package com.dubbo.example.provider.mq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "string-topic", consumerGroup = "string_consumer", selectorExpression = "*")
public class StringConsumer implements RocketMQListener<String> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private RedisTemplate<String, String> template;

    @Override
    public void onMessage(String message) {
        logger.info("------- StringConsumer received: {} \n", message);

        RMap<String, String> m = redisson.getMap("test", StringCodec.INSTANCE);
        m.put("1", "2");

        BoundHashOperations<String, String, String> hash = template.boundHashOps("test");
        String t = hash.get("1");
        logger.info("*******redis:{}********",t.equals("2"));

        logger.info(template.boundSetOps("a").getKey());
    }
}