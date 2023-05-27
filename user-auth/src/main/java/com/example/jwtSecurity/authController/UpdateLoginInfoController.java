package com.example.jwtSecurity.authController;

import com.example.jwtSecurity.config.JwtService;
import com.example.jwtSecurity.models.AutheticationRequest;
import com.example.jwtSecurity.models.UserProfile;
import com.example.jwtSecurity.services.AuthentificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/update")
@RequiredArgsConstructor
public class UpdateLoginInfoController {

    private final AuthentificationRepository repo;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationController authenticationController;

    private final JwtService jwtService;

    @PostMapping("/password")
    public @ResponseBody ResponseEntity updateUserPassword(@RequestParam String oldPassword,
                                                      @RequestParam String newPassword,
                                                      @RequestHeader("Authorization") String token){
        UserProfile user=findProfileByToken(token);
        System.out.println("call succes");
        if(passwordEncoder.matches(oldPassword, user.getPassword())){
            String password=passwordEncoder.encode(newPassword);
            user.setPassword(password);
            repo.save(user);
            System.out.println("password updated succesfuly");
            return authenticationController.authenticate(
                    AutheticationRequest.builder()
                            .email(user.getEmail())
                            .password(newPassword)
                            .build()
            );

        }
        return ResponseEntity.badRequest().body("Password Incorrect");
    }


    @PostMapping("/email")
    public @ResponseBody ResponseEntity updateUserEmail(@RequestParam String newEmail,
                                                                @RequestParam String password,
                                                                @RequestHeader("Authorization") String token){

        UserProfile user=findProfileByToken(token);
        var response ="Password Incorrect";
        if (repo.findByEmail(newEmail).isPresent()){response= "Email already in use";}
        if(passwordEncoder.matches(password, user.getPassword())) {
            user.setEmail(newEmail);
            repo.save(user);
            System.out.println("User Email updated");
             return authenticationController.authenticate(
                    AutheticationRequest.builder()
                            .email(newEmail)
                            .password(password)
                            .build()
            );
            //return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(response);

    }

    public UserProfile findProfileByToken(String token){
        var jwtToken=token.substring(7);
        var email=jwtService.extractUserName(jwtToken);
        return repo.findByEmail(email).orElseThrow();
    }
}
