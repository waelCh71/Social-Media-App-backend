package com.example.messageServices.Repository;

import com.example.messageServices.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message,String> {

    //Optional<List<ChatMessage>> findByTypeMsg(TypeMsg typeMsg);

    //Optional<List<ChatMessage>> findByMessageValue(String messageValue);
}
