package com.example.postServices.models;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class AcceuilPost {

    private String id;

    private PostType type;

    private String title;

    private Audiance audiance;

    private Date createdAt;

    private String text;

    private String urlMedia;

    private String userId;

    private String userName;

    private String userProfilePic;

    private String userStatut;

    private boolean isBlocked;

    private int nbReactions;

    private int nbComments;


    private List<Reaction> reactions;

    private List<Comments> comments;


}
