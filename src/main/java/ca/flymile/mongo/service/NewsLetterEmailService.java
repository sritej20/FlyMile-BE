package ca.flymile.mongo.service;

import ca.flymile.mongo.dto.model.NewsLetterEmailDto;

public interface NewsLetterEmailService {
    void addEmailToNewsLetter(NewsLetterEmailDto emailDto);
    void removeEmailFromNewsLetter(NewsLetterEmailDto emailDto);
    boolean isEmailSubscribed(NewsLetterEmailDto emailDto);

}
