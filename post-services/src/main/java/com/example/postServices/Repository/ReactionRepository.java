package com.example.postServices.Repository;

import com.example.postServices.models.Reaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReactionRepository extends MongoRepository<Reaction,String> {
    Optional<Reaction> findById(String id);
}
