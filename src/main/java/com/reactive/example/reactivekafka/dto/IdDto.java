package com.reactive.example.reactivekafka.dto;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IdDto {

  private UUID id;
}
