package com.example.mediaServices.Repository;

import com.example.mediaServices.models.UserMedia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MediaRepository extends MongoRepository<UserMedia,String> {
    Optional<UserMedia> findByUserId(String id);
}
