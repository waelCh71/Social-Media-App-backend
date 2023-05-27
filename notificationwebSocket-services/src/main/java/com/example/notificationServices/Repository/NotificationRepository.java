package com.example.notificationServices.Repository;

import com.example.notificationServices.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationRepository extends MongoRepository<Notification,String> {

    Optional<Notification> findByUserId(String userId);
}
