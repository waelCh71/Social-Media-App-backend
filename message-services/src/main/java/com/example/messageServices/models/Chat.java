package com.example.messageServices.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "User_Chat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Chat {
    @Id
    private String id;

    private String userOne;

    //private UserChatInfo userOneInfo;

    private String userTwo;

    //private UserChatInfo userTwoInfo;

    private List<Message> chat;


}
