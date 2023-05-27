package com.example.jwtSecurity.token;

import com.example.jwtSecurity.models.UserProfile;
import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Users_Token")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityScan
public class Token {
    @Id
    private String id;

    private String token;

    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    private String userID;

    public Token(String token, TokenType tokenType, boolean expired, boolean revoked, String userID) {
        this.token = token;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
        this.userID = userID;
    }
}
