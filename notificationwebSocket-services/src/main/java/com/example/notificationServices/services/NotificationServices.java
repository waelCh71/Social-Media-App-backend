package com.example.notificationServices.services;


import com.example.jwtSecurity.models.UserProfile;
import com.example.notificationServices.Repository.NotificationRepository;
import com.example.notificationServices.Repository.UserNotificationRepository;
import com.example.notificationServices.Repository.UserProfileRepository;
import com.example.notificationServices.models.Notification;
import com.example.notificationServices.models.UserNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class NotificationServices {

    private final UserNotificationRepository userNotificationRepository;

    private final NotificationRepository notificationRepository;

    private final UserProfileRepository userRepository;

    public ResponseEntity<String> saveNotification(String toUser, Notification request){

        UserProfile user=findProfileById(request.getUserId());
        Notification newNotification=new Notification(
                toUser,
                request.getUserId(),
                user.getFullName(),
                user.getProfile_pic_url(),
                request.getTitle(),
                request.getType(),
                new Date(System.currentTimeMillis())
        );
        var notification=notificationRepository.save(newNotification);
        notificationRepository.delete(notification);

        userNotificationRepository.findByUserId(toUser).ifPresentOrElse(userNotification -> {
            var newList=userNotification.getNotifications();
            if(newList==null){
                newList= List.of(notification);
                userNotification.setNotifications(newList);
            }else {
                newList.add(0,notification);
                userNotification.setNotifications(newList);
            }
            userNotificationRepository.save(userNotification);
        }, ()->{
            UserNotification newObject=new UserNotification(toUser,List.of(notification));
            userNotificationRepository.save(newObject);
        });

        return ResponseEntity
                .ok()
                .body("OK");
    }

    public ResponseEntity<List<Notification>> getAllNotification(String idUser){
        AtomicReference<List<Notification>> newList=new AtomicReference<List<Notification>>();
        userNotificationRepository.findByUserId(idUser).ifPresent(userNotification -> {
             newList.set(userNotification.getNotifications());
        });
        //System.out.println(newList);
        return ResponseEntity.ok().body(newList.get());
    }



    public UserProfile findProfileById(String id){
        return userRepository.findById(id).orElseThrow();
    }
}
