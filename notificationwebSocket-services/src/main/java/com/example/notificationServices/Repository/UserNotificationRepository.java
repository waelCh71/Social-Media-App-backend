package com.example.notificationServices.Repository;

import com.example.notificationServices.models.UserNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserNotificationRepository extends MongoRepository<UserNotification,String> {

    Optional<UserNotification> findByUserId(String userId);
}
