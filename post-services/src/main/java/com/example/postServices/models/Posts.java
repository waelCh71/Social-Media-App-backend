package com.example.postServices.models;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Posts {

    @Id
    private String id;

    private PostType type;

    private String title;

    private Audiance audiance;

    private Date createdAt;

    private String text;

    private String urlMedia;

    private String userId;

    private boolean isBlocked;

    private int nbReactions;

    private int nbComments;

    private boolean isReactedByUser;
    private List<Reaction> reactions;

    private List<Comments> comments;

    public Posts(PostType type, String title, Audiance audiance, Date createdAt, String text, String urlMedia, String userId,
                 boolean isBlocked,int nbReactions, int nbComments,boolean isReactedByUser, List<Reaction> reactions, List<Comments> comments) {
        this.type = type;
        this.title = title;
        this.audiance = audiance;
        this.createdAt = createdAt;
        this.text = text;
        this.urlMedia = urlMedia;
        this.userId=userId;
        this.isBlocked = isBlocked;
        this.nbReactions = nbReactions;
        this.nbComments = nbComments;
        this.isReactedByUser=isReactedByUser;
        this.reactions = reactions;
        this.comments = comments;
    }
}
