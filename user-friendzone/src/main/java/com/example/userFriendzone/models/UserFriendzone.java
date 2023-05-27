package com.example.userFriendzone.models;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@Document(collection = "User_Friendzone")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityScan
public class UserFriendzone {

    @Id
    private  String id;

    private String userID;

    private List<String> friendRequestList;

    private List<String> requestSentList;

    private List<String> blockedUser;

    private List<String> friendList;

}
