package com.example.privateChatServices.Repository;

import com.example.privateChatServices.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends MongoRepository<Chat,String> {
    Optional<List<Chat>> findByUserOne(String idUserOne);
    Optional<List<Chat>> findByUserTwo(String idUserTwo);
}