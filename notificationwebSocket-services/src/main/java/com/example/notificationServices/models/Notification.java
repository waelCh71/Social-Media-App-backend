package com.example.notificationServices.models;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {

    @Id
    private String id;

    private String to;
    private String userId;

    private String userName;

    private String userProfile;

    private String title;

    private String type;

    private Date createdAt;

    public Notification(String to,String userId, String userName, String userProfile, String title, String type,Date createdAt) {
        this.to=to;
        this.userId = userId;
        this.userName = userName;
        this.userProfile = userProfile;
        this.title = title;
        this.type = type;
        this.createdAt=createdAt;
    }

    public  enum NotificationType{
        NEWCOMMENT,
        NEWREACTION,
        SENDREQUEST,
        ACCEPTREQUEST
    }
}
