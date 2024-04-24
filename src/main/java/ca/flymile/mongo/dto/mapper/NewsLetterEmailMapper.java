package ca.flymile.mongo.dto.mapper;

import ca.flymile.mongo.dto.model.NewsLetterEmailDto;
import ca.flymile.mongo.model.NewsLetterEmail;

public class NewsLetterEmailMapper {
    public static NewsLetterEmailDto toDto(NewsLetterEmail newsLetterEmail) {
        return new NewsLetterEmailDto()
                .setEmail(newsLetterEmail.getEmail());
    }
}
