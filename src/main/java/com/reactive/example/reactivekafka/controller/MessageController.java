package com.reactive.example.reactivekafka.controller;

import com.reactive.example.reactivekafka.dto.ChatEventDto;
import com.reactive.example.reactivekafka.dto.IdDto;
import com.reactive.example.reactivekafka.dto.MessageCreateDto;
import com.reactive.example.reactivekafka.dto.MessageDto;
import com.reactive.example.reactivekafka.mapper.ChatEventMapper;
import com.reactive.example.reactivekafka.mapper.MessageMapper;
import com.reactive.example.reactivekafka.service.MessageService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;
  private final MessageMapper messageMapper;
  private final ChatEventMapper chatEventMapper;

  @PostMapping("api/v1/reactive-kafka/messages/{chat-id}/author/{author}")
  public IdDto create(
      @PathVariable("chat-id") Long chatId,
      @PathVariable("author") String author,
      @Valid @RequestBody MessageCreateDto messageDto
  ) {
    var createdMessage = messageService
        .createMessage(messageMapper.createDtoToModel(messageDto, author, chatId));
    return new IdDto()
        .setId(createdMessage.getMessageId());
  }

  @GetMapping(value = "api/v1/reactive-kafka/messages/{chat-id}",
      produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<MessageDto> streamMessages(
      @PathVariable("chat-id") Long chatId
  ) {
    return messageService.streamMessagesForChat(chatId)
        .map(messageMapper::modelToResponseDto);
  }

  @GetMapping(value = "api/v2/reactive-kafka/messages/{chat-id}",
      produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ChatEventDto> streamChatEvents(
      @PathVariable("chat-id") Long chatId,
      @RequestParam(value = "username", defaultValue = "unknown") String username
  ) {
    return messageService.streamChatEvents(chatId, username)
        .map(chatEventMapper::modelToChatEventDto);
  }
}
