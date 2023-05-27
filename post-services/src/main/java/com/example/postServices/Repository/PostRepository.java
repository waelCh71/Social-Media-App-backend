package com.example.postServices.Repository;

import com.example.postServices.models.Posts;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PostRepository extends MongoRepository<Posts,String> {
    Optional<Posts> findById(String id);

}
