package com.example.privateChatServices.controllers;


import com.example.privateChatServices.models.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RequestMapping("/api4/v1/msg-services")
//@CrossOrigin(origins = "http://localhost:4200")

@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", allowCredentials = "true")
public class PrivateChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/application") //to = nome canale
    @SendTo("/all/messages")
    public Message send(final Message message) throws Exception{
        return message;
    }

    @MessageMapping("/private") //to = nome canale
    @SendToUser("/specific/messages")
    public void sendToUser(final Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getIdReceiver(),"/specific",message);
    }

    @GetMapping
    public String demoController(){
        return "Hello from secured end point";
    }
}
