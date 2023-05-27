package com.example.postServices.models;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reaction {

    @Id
    private String id;

    private ReactType type;

    private String userId;

    private String userName;

    private String userProfilePic;

    public enum ReactType{
        LIKE,
        LOVE,
        LAUGH,
        WOW,
        ANGRY,
        SAD,
    }
}
