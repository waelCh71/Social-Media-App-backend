package com.example.jwtSecurity.authController;

import com.example.jwtSecurity.services.AuthenticationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkConnectivity")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class demoController {

    private final AuthenticationServices services;

    @GetMapping
    public String sayHello(
            @RequestHeader("Authorization") String token
    ){
        System.out.println("11 "+token);
        return "Hello from secure end point";
    }



}
