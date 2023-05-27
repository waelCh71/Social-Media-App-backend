package com.example.privateChatServices.Repository;

import com.example.privateChatServices.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message,String> {

    //Optional<List<ChatMessage>> findByTypeMsg(TypeMsg typeMsg);

    //Optional<List<ChatMessage>> findByMessageValue(String messageValue);
}
