package com.example.notificationServices.controllers;

import com.example.notificationServices.models.Notification;
import com.example.notificationServices.services.NotificationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private NotificationServices notificationService;

    @MessageMapping("/application") //to = nome canale
    @SendTo("/all/notification")
    public Notification send(final Notification notification) throws Exception{
        return notification;
    }


    @PostMapping("/saveNotification")
    public ResponseEntity<String> saveNotification (@RequestParam String toUser, @RequestBody Notification notification){
        System.out.println("notification work");
        return notificationService.saveNotification(toUser, notification);
    }

    @PostMapping("/getAllNotification")
    public ResponseEntity<List<Notification>> getAllNotification (@RequestParam String idUser){
        System.out.println("notification work");
        return notificationService.getAllNotification(idUser);
    }

    @GetMapping
    public String demoController(){
        return "Hello from secured end point";
    }
}
