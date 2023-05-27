package com.example.messageServices.controllers;


import com.example.messageServices.models.Chat;
import com.example.messageServices.models.Message;
import com.example.messageServices.services.MessageServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api4/v1/msg-services")
//@CrossOrigin(origins = "http://localhost:4200")

@RequiredArgsConstructor

public class ChatController {

    @Autowired
    private MessageServices messageServices;

    //@Autowired
    //private SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/chat/{to}") //to = nome canale
    @SendTo("/topic/{to}")
    public void sendMessage(@DestinationVariable String to , Message message) {
        System.out.println("handling send message: " + message + " to: " + to);
        messageServices.sentMessageToUser(to, message);
    }

    @PostMapping("/saveMessage")
    public ResponseEntity<String> saveMessage( @RequestParam String idReceiver,
                                              @RequestBody Message message,@RequestHeader("Authorization") String token){
        return messageServices.saveMessageToChat(token, idReceiver, message);

    }

    @PostMapping("/getChats")
    public ResponseEntity<List<Chat>> getChats(@RequestHeader("Authorization") String token){

        return messageServices.getAllChats(token);
    }

    @PostMapping("/getMessages")
    public ResponseEntity<List<Message>> getMessages(@RequestParam String idUser1, @RequestHeader("Authorization") String token){
        return messageServices.getAllMessages(idUser1, token);
    }

    @GetMapping
    public String demoController(){
        return "Hello from secured end point";
    }
}
