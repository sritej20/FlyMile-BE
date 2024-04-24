package ca.flymile.mongo.service;

import ca.flymile.mongo.dto.model.MessageDto;

import java.util.List;

public interface MessageService {
    void addMessage(MessageDto messageDto);
    List<MessageDto> getMessageForEmail(String email);
}
