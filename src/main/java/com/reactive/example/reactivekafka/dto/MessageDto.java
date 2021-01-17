package com.reactive.example.reactivekafka.dto;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageDto {

  private UUID id;
  private String message;
  private String from;
  private Instant creationTime;
}
