package com.ym.provider.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author: ym
 * @Description:
 * @Date: 2020/2/28 1:23 下午
 * @Version:
 */
@Component
@Slf4j
public class TestReceiver {
    /**
     * listenerContainerFactory可批量拉取消息，参数是List<ConsumerRecord<Integer, String>>，否则是ConsumerRecord
     * 注意第二个参数Acknowledgment，这个参数只有在设置消费者的ack应答模式为AckMode.MANUAL_IMMEDIATE才能注入，意思是需要手动ack。
     * acknowledgment.acknowledge();
     * topic是数组，可以同时监听多个，根据不同topic处理不同逻辑
     * @param
     * @param
     */
    @KafkaListener(topics = {"ym"}, containerFactory = "kafkaListenerContainerFactory")
    public void registryReceiver(ConsumerRecord<?, ?> integerStringConsumerRecords) {
        log.info("消息:{}",integerStringConsumerRecords.value());
        log.info("消息:{}",integerStringConsumerRecords.headers());
        log.info("消息:{}",integerStringConsumerRecords.key());
        log.info("消息:{}",integerStringConsumerRecords.topic());
    }

    /**
     * 消费者参数也可以直接为String,其值为消息value()
     * @param msg
     */
    @KafkaListener(topics = {"test"}, containerFactory = "kafkaListenerContainerFactory")
    public void registryReceiver(String msg) {
        log.info("消息:{}",msg);
    }
}
