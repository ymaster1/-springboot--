package com.ym.provider.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ym
 * @Description:
 * @Date: 2020/2/28 12:45 下午
 * @Version:
 */
@Configuration
@EnableKafka
@Slf4j
public class KafkaConfig {
    /**
     * 如果解注解注入会使用默认的构造器,使用的配置为配置文件中的配置 KafkaAutoConfiguration
     * @Autowired
     *     private KafkaTemplate kafkaTemplate;
     */


    /**
     * kafka服务器地址
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * 构造消费者属性map，ConsumerConfig中的可配置属性比spring boot自动配置要多
     *
     * @return
     */
    private Map<String, Object> consumerProperties() {
        Map<String, Object> props = new HashMap<>();
        //自动提交offset
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
        //消费者组id
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "activity-service");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return props;
    }

    /**
     * 消费者工厂
     *
     * @return
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProperties());
    }

    /**
     * 消费者的监听容器工厂方法 以方法名kafkaListenerContainerFactory作为bean 可以定义多个用于满足不同个性化需求，在相应的消费者上注入即可
     *
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        //可以多个线程
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        //设置消费者工厂
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(1);
        //设置手动ACK
        //factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        factory.setMissingTopicsFatal(false);
        //设置可批量拉取消息消费，拉取数量，看需求设置，监听器拉取到的是可以是一个list<>
//        factory.setBatchListener(true);
        log.info("init kafkaListener annotation container Factory");
        return factory;
    }

    /**
     * 创建生产者配置map，ProducerConfig中的可配置属性比spring boot自动配置要多
     *
     * @return
     */
    private Map<String, Object> producerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "-1");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 5);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 500);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return props;
    }

    /**
     * 不使用spring boot的KafkaAutoConfiguration默认方式创建的DefaultKafkaProducerFactory，重新定义
     *
     * @return
     */
    @Bean
    public DefaultKafkaProducerFactory produceFactory() {
        DefaultKafkaProducerFactory factory = new DefaultKafkaProducerFactory(producerProperties());
        //2.3开始加的新属性，可以为每个客户端创建一个生产者

        return factory;
    }

    /**
     * 不使用spring boot的KafkaAutoConfiguration默认方式创建的KafkaTemplate，重新定义
     *
     * @param produceFactory
     * @return
     */
    @Bean
    public KafkaTemplate kafkaTemplate(DefaultKafkaProducerFactory produceFactory) {
        return new KafkaTemplate(produceFactory);
    }


}
