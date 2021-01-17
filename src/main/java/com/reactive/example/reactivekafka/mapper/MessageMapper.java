package com.reactive.example.reactivekafka.mapper;

import com.reactive.example.reactivekafka.dto.MessageCreateDto;
import com.reactive.example.reactivekafka.dto.MessageDto;
import com.reactive.example.reactivekafka.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CommonMapperConfig.class)
public interface MessageMapper {

  @Mapping(target = "messageId", expression = "java(java.util.UUID.randomUUID())")
  @Mapping(target = "creationTime", expression = "java(java.time.Instant.now())")
  Message createDtoToModel(MessageCreateDto source, String author, long chatId);

  @Mapping(target = "id", source = "messageId")
  @Mapping(target = "from", source = "author")
  MessageDto modelToResponseDto(Message source);
}
