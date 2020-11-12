package com.ym.provider.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @Author: ym
 * @Description: kafka消息发送工具
 * @Date: 2020/2/28 9:56 下午
 * @Version:
 */
@Component
@Slf4j
public class KafkaProducer<K, V> {
    /**
     * 这里注入的就是自己在config定义的KafkaTemplate
     */
    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 异步发送消息,能监听发送结果
     *
     * @param topicName
     * @param msg
     * @return
     */
    public ListenableFuture<SendResult<K, V>> send(String topicName, V msg) {
        //可以设置消息发送的监听器，能监听消息发送的状态（回调）；可选的功能，默认配置LoggingProducerListener（），只记录错误信息
        kafkaTemplate.setProducerListener(this.producerListener);

        //kafkaTemplate.execute((KafkaOperations.ProducerCallback<String, String, Object>) producer -> {
        //    //这里可以编写kafka原生的api操作
        //    return null;
        //});

        //最终是发送一个ProducerRecord
        return kafkaTemplate.send(topicName, msg);


    }

    /**
     * 用于监听发送状况，可以不要
     */
    private ProducerListener producerListener = new ProducerListener<K, V>() {
        @Override
        public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {

        }

        @Override
        public void onSuccess(String topic, Integer partition, Object key, Object value,
                              RecordMetadata recordMetadata) {

        }

        @Override
        public void onError(ProducerRecord producerRecord, Exception exception) {

        }

        @Override
        public void onError(String topic, Integer partition, Object key, Object value,
                            Exception exception) {

        }
    };
}
