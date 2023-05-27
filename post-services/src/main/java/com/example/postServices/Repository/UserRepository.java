package com.example.postServices.Repository;

import com.example.jwtSecurity.models.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserProfile,String> {

    Optional<UserProfile> findByEmail(String email);
    Optional<List<UserProfile>> findByCity(String city);
    Optional<List<UserProfile>> findByFirstname(String firstName);
    Optional<List<UserProfile>> findByLastname(String lastName);
    Optional<List<UserProfile>> findByPhoneNumber(long phone);

}
