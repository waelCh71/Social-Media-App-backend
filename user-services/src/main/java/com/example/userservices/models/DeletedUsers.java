package com.example.userservices.models;

import com.example.jwtSecurity.models.UserProfile;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Deleted_Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeletedUsers extends UserProfile {

    private Date deletedDate;

    //private String deletedEmail;

    private UserFriendzone userFriendzone;

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }
}
