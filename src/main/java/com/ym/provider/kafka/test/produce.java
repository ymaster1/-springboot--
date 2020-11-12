package com.ym.provider.kafka.test;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * 使用最原生的kafka创建生产者
 */
public class produce {
    public static void main(String[] args) {
        Properties properties = new Properties();
        //设置key序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);
        //值序列化器
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "39.97.248.212:9092");

        //生产者
        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);
        //对发送的内容（topic,key,value）进行封装
        ProducerRecord<String,String> record = new ProducerRecord<>("ymaster1","Name","papi" );
        try{
            //同步发送，返回发送结果
            Future<RecordMetadata> result = producer.send(record);
            RecordMetadata recordMetadata = result.get();
            System.out.println("topic :"+recordMetadata.topic());
            System.out.println("offset :"+recordMetadata.offset());
            System.out.println("partition :"+recordMetadata.partition());

            //异步发送，主线程可以继续执行业务，发送完了会自动回调，输出结果
//            producer.send(record, new Callback() {
//                @Override
//                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                    if (e == null){
//                        System.out.println("topic :"+recordMetadata.topic());
//                        System.out.println("offset :"+recordMetadata.offset());
//                        System.out.println("partition :"+recordMetadata.partition());
//                    }
//                }
//            });
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            producer.close();
        }

    }
}
