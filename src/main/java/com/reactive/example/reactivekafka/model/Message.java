package com.reactive.example.reactivekafka.model;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Message {

  private UUID messageId;
  private long chatId;
  private String author;
  private String message;
  private Instant creationTime;
}
