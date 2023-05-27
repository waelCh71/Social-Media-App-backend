package com.example.jwtSecurity.authController;


import com.example.jwtSecurity.models.AutheticationRequest;
import com.example.jwtSecurity.models.UserProfile;
import com.example.jwtSecurity.services.AuthenticationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServices services;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserProfile request){

        return services.register(request);
    }


    @PostMapping("/authenticate")
    public ResponseEntity<String>  authenticate(@RequestBody AutheticationRequest request){
        return services.authenticate(request);
    }

    @PostMapping("/checkToken")
    public ResponseEntity<String> checkTokenValidity(@RequestHeader("Authorization") String token){
        return services.checkTokenValidityAndConnect(token);
    }



}
