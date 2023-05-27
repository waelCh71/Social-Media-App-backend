package com.example.postServices.Repository;

import com.example.postServices.models.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface CommentsRepository extends MongoRepository<Comments,String> {
    Optional<Comments> findById(String id);

}
