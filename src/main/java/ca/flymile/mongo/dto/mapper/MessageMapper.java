package ca.flymile.mongo.dto.mapper;

import ca.flymile.mongo.dto.model.MessageDto;
import ca.flymile.mongo.model.Message;

public class MessageMapper {
    public static MessageDto toDto(Message message) {
        return new MessageDto()
                .setName(message.getName())
                .setMessage(message.getMessage())
                .setEmail(message.getEmail());
    }
}
