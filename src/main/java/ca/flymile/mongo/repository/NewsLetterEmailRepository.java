package ca.flymile.mongo.repository;

import ca.flymile.mongo.model.NewsLetterEmail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsLetterEmailRepository extends MongoRepository<NewsLetterEmail, String> {
    void deleteByEmail(String email);
    boolean existsByEmail(String email);
}
