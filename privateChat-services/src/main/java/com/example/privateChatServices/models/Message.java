package com.example.privateChatServices.models;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {

    @Id
    private  String id;

    private TypeMsg typeMsg;

    private Date sendDate;

    private String idReceiver;

    private String idSender;

    private String messageValue;

    public Message(TypeMsg typeMsg, Date sendDate, String idSender, String messageValue) {
        this.typeMsg = typeMsg;
        this.sendDate = sendDate;
        this.idSender = idSender;
        this.messageValue = messageValue;
    }

    /*private String content;
    private String sender;
    private MessageType type;

    public enum MessageType{
        CHAT,LEAVE,JOIN
    }*/
}
