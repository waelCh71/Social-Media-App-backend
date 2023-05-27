package com.example.postServices.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "User_Post")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserPost {
    @Id
    private String id;

    private String userID;

    private List<Posts> posts;

    public UserPost(String userID, List<Posts> posts) {
        this.userID = userID;
        this.posts = posts;
    }
}
