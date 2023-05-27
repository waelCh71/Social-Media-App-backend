package com.example.jwtSecurity.services;

import com.example.jwtSecurity.token.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token,String> {

    List<Token> findAllValidTokensByUserID(String userId);

    Optional<Token> findByToken(String token);

}
