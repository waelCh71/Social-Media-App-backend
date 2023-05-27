package com.example.postServices.Repository;

import com.example.postServices.models.Posts;
import com.example.postServices.models.UserPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserPostRepository extends MongoRepository<UserPost,String> {
    Optional<UserPost> findById(String id);
    Optional<UserPost> findByUserID(String id);

    Optional<UserPost> findByPosts(Posts post);
}
