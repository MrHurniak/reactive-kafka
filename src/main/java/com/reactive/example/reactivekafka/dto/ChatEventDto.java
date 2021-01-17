package com.reactive.example.reactivekafka.dto;

import com.reactive.example.reactivekafka.ChatEventType;
import java.time.Instant;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatEventDto {

  private ChatEventType eventType;
  private Map<String, Object> parameters;
  private Instant creationTime;
}
