package ca.flymile.mongo.service.implementation;

import ca.flymile.mongo.dto.model.MessageDto;
import ca.flymile.mongo.model.Message;
import ca.flymile.mongo.repository.MessageRepository;
import ca.flymile.mongo.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    @Override
    public void addMessage(MessageDto messageDto) {
        messageRepository.save(modelMapper.map(messageDto, Message.class));
    }

    @Override
    public List<MessageDto> getMessageForEmail(String email) {
        return messageRepository.findByEmail(email)
                .stream()
                .map(message -> modelMapper.map(message, MessageDto.class))
                .collect(Collectors.toList());
    }
}
