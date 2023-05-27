package com.example.messageServices.services;


import com.example.jwtSecurity.models.UserProfile;
import com.example.messageServices.Repository.ChatRepository;
import com.example.messageServices.Repository.MessageRepository;
import com.example.messageServices.Repository.UserRepository;
import com.example.messageServices.config.JwtService;
import com.example.messageServices.models.Chat;
import com.example.messageServices.models.Message;
import com.example.messageServices.models.TypeMsg;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class MessageServices {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private final JwtService jwtService;


    private final ChatRepository chatRepository;

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;



    public void sentMessageToUser(String idReceiver, Message message){
        System.out.println("handling send message: " + message + " to: " + idReceiver);
        String idSender= message.getIdSender();
        message.setSendDate(new Date(System.currentTimeMillis()));
        //saveMessageToChat(message.getIdSender(), idReceiver,message);
        simpMessagingTemplate.convertAndSend("/topic/messages/" + idReceiver, message);
    }

    public ResponseEntity<String> saveMessageToChat(String token, String idReceiver, Message request){
        AtomicReference<String> response=new AtomicReference<>("Something went wrong");

        var idSender=findProfileByToken(token).getId();

        Message messageModel=new Message(
                TypeMsg.TEXT,
                new Date(System.currentTimeMillis()),
                idSender,
                request.getMessageValue());
        var message=messageRepository.save(messageModel);
        messageRepository.delete(message);
        chatRepository.findById(idSender+idReceiver).ifPresentOrElse(chat -> {
                    System.out.println(chat);
                    if (chat.getUserTwo().equals(idReceiver)){
                        List<Message> newList=chat.getChat();
                        newList.add(message);
                        chat.setChat(newList);
                        chatRepository.save(chat);
                        response.set("Message saved");
                        return;
                    }
        },
                ()->{

            chatRepository.findById(idReceiver+idSender).ifPresentOrElse(chat -> {
                        if (chat.getUserTwo().equals(idSender)){
                            List<Message> newList=chat.getChat();
                            newList.add(message);
                            chat.setChat(newList);
                            chatRepository.save(chat);
                            response.set("Message saved");
                            return;
                        }
            },
                    ()->{
                        System.out.println(idSender+idReceiver);
                        Chat newChat=new Chat(idSender+idReceiver,idSender,idReceiver,List.of(message));
                        chatRepository.insert(newChat);
                        response.set("Message saved");
                    });
                });
        if(String.valueOf(response).equals("Message saved")){
            return ResponseEntity.ok().body("Message saved");
        }
        return ResponseEntity.badRequest().body("");
    }

    public ResponseEntity<List<Chat>> getAllChats(String token){
        var idUser=findProfileByToken(token).getId();
        List<Chat> chatList = new ArrayList<>();
        if (chatRepository.findByUserOne(idUser).isPresent()){
            System.out.println("test");

            chatList.addAll(chatRepository.findByUserOne(idUser).orElseThrow());
        }
        if (chatRepository.findByUserTwo(idUser).isPresent()) {
            chatList.addAll(chatRepository.findByUserTwo(idUser).orElseThrow());
        }
        return ResponseEntity.ok().body(chatList);
    }

    public ResponseEntity<List<Message>> getAllMessages(String idUser1, String token){

        var idUser2=findProfileByToken(token).getId();
        AtomicReference<List<Message>> messageList= new AtomicReference<List<Message>>(new ArrayList<>());
        chatRepository.findById(idUser1+idUser2).ifPresentOrElse(chat -> {
            messageList.set(chat.getChat());
        },()->{
            chatRepository.findById(idUser2+idUser1).ifPresent(chat -> {
                messageList.set(chat.getChat());
            });
        });
        return ResponseEntity.ok().body(messageList.get());
    }

    public UserProfile findProfileByToken(String token){
        var jwtToken=token.substring(7);
        var email=jwtService.extractUserName(jwtToken);
        return userRepository.findByEmail(email).orElseThrow();
    }
}

