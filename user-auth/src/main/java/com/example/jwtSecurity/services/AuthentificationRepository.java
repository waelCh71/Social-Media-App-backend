package com.example.jwtSecurity.services;

import com.example.jwtSecurity.models.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface AuthentificationRepository extends MongoRepository<UserProfile,String> {
    Optional<UserProfile> findByEmail(String email);
    Optional<UserProfile> findByPhoneNumber(String phone);
}
