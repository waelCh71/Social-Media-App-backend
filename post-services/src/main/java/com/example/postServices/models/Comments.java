package com.example.postServices.models;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comments {

    @Id
    private String id;

    private String userId;

    private String userName;

    private String text;

    private String urlMedia;

    private Date createdAt;
}
