package com.example.notificationServices.Repository;

import com.example.jwtSecurity.models.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProfileRepository extends MongoRepository<UserProfile,String> {
}
