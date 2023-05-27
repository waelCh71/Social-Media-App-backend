package com.example.userservices.Services;

import com.example.jwtSecurity.token.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token,String> {

    List<Token> findAllValidTokensByUserID(String userId);

    Optional<Token> findByToken(String token);
}
