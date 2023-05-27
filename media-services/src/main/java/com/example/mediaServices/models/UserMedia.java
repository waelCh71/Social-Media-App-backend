package com.example.mediaServices.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "User_Media")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserMedia {
    @Id
    private String id;

    private String userId;

    private Media profile_pic;

    private Media cover_pic;

    private List<Media> messageMedia;

    private List<Media> postMedia;
}
