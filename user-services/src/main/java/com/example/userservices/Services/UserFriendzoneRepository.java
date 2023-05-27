package com.example.userservices.Services;

import com.example.userservices.models.UserFriendzone;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserFriendzoneRepository extends MongoRepository<UserFriendzone,String> {

    Optional<UserFriendzone> findByUserID(String s);

}
