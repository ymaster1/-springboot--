package com.ym.provider.kafka.test;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * 使用最原生的kafka创建消费者
 */
public class consume {
    public static void main(String[] args) {
        Properties properties = new Properties();
        //key反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //值反序列化
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "39.97.248.212:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group");

        //消费者
        KafkaConsumer<String,String> consumer = new KafkaConsumer<>(properties);
        //订阅主题
        consumer.subscribe(Collections.singletonList("ymaster1"));
        while (true){
            ConsumerRecords<String,String> consumerRecord = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String,String> records : consumerRecord){
                System.out.println(records.value());
                System.out.println(records.key());
            }
        }

    }
}
