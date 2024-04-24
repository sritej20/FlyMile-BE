package ca.flymile.mongo.service.implementation;

import ca.flymile.mongo.dto.model.NewsLetterEmailDto;
import ca.flymile.mongo.model.NewsLetterEmail;
import ca.flymile.mongo.repository.NewsLetterEmailRepository;
import ca.flymile.mongo.service.NewsLetterEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class NewsLetterEmailServiceImpl implements NewsLetterEmailService {
    private final NewsLetterEmailRepository newsLetterEmailRepository;
    private final ModelMapper modelMapper;

    @Override
    public void addEmailToNewsLetter(NewsLetterEmailDto emailDto) {
        newsLetterEmailRepository.save(modelMapper.map(emailDto, NewsLetterEmail.class));
    }

    @Override
    public void removeEmailFromNewsLetter(NewsLetterEmailDto emailDto) {
        newsLetterEmailRepository.deleteByEmail(emailDto.getEmail());
    }

    @Override
    public boolean isEmailSubscribed(NewsLetterEmailDto emailDto) {
        return newsLetterEmailRepository.existsByEmail(emailDto.getEmail());
    }
}
