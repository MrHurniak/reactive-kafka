package com.reactive.example.reactivekafka.mapper;

import com.reactive.example.reactivekafka.dto.ChatEventDto;
import com.reactive.example.reactivekafka.model.ChatEvent;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapperConfig.class)
public interface ChatEventMapper {

  ChatEventDto modelToChatEventDto(ChatEvent source);
}
