package com.example.userFriendzone.Services;

import com.example.userFriendzone.models.UserFriendzone;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserFriendzoneRepository extends MongoRepository<UserFriendzone,String> {

    Optional<UserFriendzone> findByUserID(String s);

}
