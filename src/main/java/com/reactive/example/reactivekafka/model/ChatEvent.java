package com.reactive.example.reactivekafka.model;

import com.reactive.example.reactivekafka.ChatEventType;
import java.time.Instant;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatEvent {

  private long chatId;
  private ChatEventType eventType;
  private Instant creationTime;
  private Map<String, Object> parameters;
}
