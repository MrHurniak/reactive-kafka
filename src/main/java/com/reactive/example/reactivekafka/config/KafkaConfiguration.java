package com.reactive.example.reactivekafka.config;

import com.reactive.example.reactivekafka.model.ChatEvent;
import com.reactive.example.reactivekafka.model.Message;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;

@Configuration
public class KafkaConfiguration {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean("messageKafkaReceiver")
  KafkaReceiver<String, Message> messageKafkaReceiver() {
    ReceiverOptions<String, Message> options = ReceiverOptions
        .create(provideCommonReceiverConfigMap());
    options.subscription(List.of("messages"))
        .withKeyDeserializer(new StringDeserializer())
        .withValueDeserializer(new JsonDeserializer<>(Message.class));

    return new DefaultKafkaReceiver<>(ConsumerFactory.INSTANCE, options);
  }

  @Bean("chatEventKafkaReceiver")
  KafkaReceiver<String, ChatEvent> chatEventKafkaReceiver() {
    ReceiverOptions<String, ChatEvent> options = ReceiverOptions
        .create(provideCommonReceiverConfigMap());
    options.subscription(List.of("chat-events"))
        .withKeyDeserializer(new StringDeserializer())
        .withValueDeserializer(new JsonDeserializer<>(ChatEvent.class));

    return new DefaultKafkaReceiver<>(ConsumerFactory.INSTANCE, options);
  }

  private Map<String, Object> provideCommonReceiverConfigMap() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, "client-1");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-1");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    return props;
  }

  @Bean
  KafkaTemplate<String, Object> kafkaTemplate() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    return new KafkaTemplate<>(
        new DefaultKafkaProducerFactory<>(props, new StringSerializer(), new JsonSerializer<>()));
  }
}