package ca.flymile.mongo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "massages")
public class Message {
    @MongoId
    private String _id;
    private String name;
    private String message;
    private String email;
}
