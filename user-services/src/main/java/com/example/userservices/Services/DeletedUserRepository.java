package com.example.userservices.Services;

import com.example.userservices.models.DeletedUsers;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

public interface DeletedUserRepository extends MongoRepository<DeletedUsers,String> {


}
