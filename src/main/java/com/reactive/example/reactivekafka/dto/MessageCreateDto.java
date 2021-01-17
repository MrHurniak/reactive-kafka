package com.reactive.example.reactivekafka.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageCreateDto {

  @NotBlank
  private String message;
}
