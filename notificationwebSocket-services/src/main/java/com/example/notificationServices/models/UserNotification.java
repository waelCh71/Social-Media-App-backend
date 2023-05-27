package com.example.notificationServices.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "User_Notification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserNotification {
    @Id
    private String id;

    private String userId;

    private List<Notification> notifications;

    public UserNotification(String userId, List<Notification> notifications) {
        this.userId = userId;
        this.notifications = notifications;
    }
}
