package com.reactive.example.reactivekafka.service;

import com.reactive.example.reactivekafka.ChatEventType;
import com.reactive.example.reactivekafka.model.ChatEvent;
import com.reactive.example.reactivekafka.model.Message;
import java.time.Instant;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final KafkaReceiver<String, Message> messageKafkaReceiver;
  private final KafkaReceiver<String, ChatEvent> chatEventKafkaReceiver;

  private Flux<Message> bridge;
  private Flux<ChatEvent> chatEventBridge;

  @PostConstruct
  void init() {
    bridge = messageKafkaReceiver.receive()
        .map(ConsumerRecord::value)
        .publish()
        .autoConnect();
    chatEventBridge = chatEventKafkaReceiver.receive()
        .map(ConsumerRecord::value)
        .publish()
        .autoConnect();
  }

  public Message createMessage(Message message) {
    log.info("Save message with id '{}'", message.getMessageId());
    kafkaTemplate.send("messages", message);
    kafkaTemplate.send("chat-events", provideChatEvent(message));
    return message;
  }

  public Flux<Message> streamMessagesForChat(long chatId) {
    log.info("Enter id chat with id '{}'", chatId);
    return bridge
        .filter(message -> message.getChatId() == chatId);
  }

  //Experimental. Not sure if this works correct
  public Flux<ChatEvent> streamChatEvents(long chatId, String username) {
    return chatEventBridge
        .filter(chatEvent -> chatEvent.getChatId() == chatId)
        .doFirst(() -> sendUserInEvent(chatId, username))
        .doFinally(s -> sendUserOutEvent(chatId, username));
  }

  private void sendUserInEvent(long chatId, String username) {
    log.info("User with name '{}' in into chat with id '{}'", username, chatId);
    ChatEvent userInEvent = new ChatEvent()
        .setChatId(chatId)
        .setEventType(ChatEventType.USER_IN)
        .setCreationTime(Instant.now())
        .setParameters(Map.of("username", username));
    kafkaTemplate.send("chat-events", userInEvent);
  }

  private void sendUserOutEvent(long chatId, String username) {
    log.info("User with name '{}' out from chat with id '{}'", username, chatId);
    ChatEvent userInEvent = new ChatEvent()
        .setChatId(chatId)
        .setEventType(ChatEventType.USER_OUT)
        .setCreationTime(Instant.now())
        .setParameters(Map.of("username", username));
    kafkaTemplate.send("chat-events", userInEvent);
  }

  private ChatEvent provideChatEvent(Message message) {
    return new ChatEvent()
        .setChatId(message.getChatId())
        .setEventType(ChatEventType.NEW_MESSAGE)
        .setCreationTime(message.getCreationTime())
        .setParameters(
            Map.of("text", message.getMessage(),
                "messageId", message.getMessageId()
            ));
  }
}
