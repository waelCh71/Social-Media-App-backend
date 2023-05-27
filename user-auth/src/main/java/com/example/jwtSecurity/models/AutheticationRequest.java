package com.example.jwtSecurity.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class AutheticationRequest {
    private String email;
    private String password;

    public AutheticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
