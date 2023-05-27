package com.example.messageServices.models;

import lombok.*;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserChatInfo {
    private String fullName;

    private String profilePic;

    private String userStatut;
}
