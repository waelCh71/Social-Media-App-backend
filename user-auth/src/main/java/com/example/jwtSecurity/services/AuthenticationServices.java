package com.example.jwtSecurity.services;


import com.example.jwtSecurity.config.JwtService;
import com.example.jwtSecurity.models.AutheticationRequest;
import com.example.jwtSecurity.models.Role;
import com.example.jwtSecurity.models.UserProfile;
import com.example.jwtSecurity.models.UserStatut;
import com.example.jwtSecurity.token.Token;
import com.example.jwtSecurity.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class AuthenticationServices {

    private final AuthentificationRepository authRepo;

    private final TokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public ResponseEntity<String> register(UserProfile request) {

        var password=passwordEncoder.encode(request.getPassword());
        UserProfile userProfile =new UserProfile(
                request.getFirstname(),
                request.getLastname(),
                request.getFirstname()+" "+request.getLastname(),
                request.getEmail(),
                password,
                request.getDob(),
                request.getCountry(),
                (request.getPhoneNumber()==null) ? "0" : request.getPhoneNumber(),
                (request.getCity()==null) ? "N/A" : request.getCity(),
                new Date(System.currentTimeMillis()),
                Role.USER,
                request.getGender());


        AtomicReference<String> authenticationResponse = new AtomicReference<>();
        authRepo.findByEmail(request.getEmail()).ifPresentOrElse(
                s->authenticationResponse.set("User Already Exist"),
                ()->{
                    if (!request.getPhoneNumber().equals("0") && authRepo.findByPhoneNumber(request.getPhoneNumber()).isPresent()){
                        authenticationResponse.set("Phone Already in Use");
                    }else {
                        System.out.println("Inserting User");

                        var savedUser=authRepo.insert(userProfile);
                        var jwtToken =jwtService.generateToken(savedUser);
                        saveUserToken(savedUser, jwtToken);
                        authenticationResponse.set(jwtToken);
                    }
                }

        );
        if(String.valueOf(authenticationResponse).equals("User Already Exist") &&
                String.valueOf(authenticationResponse).equals("Phone Already in Use")){
            return ResponseEntity.badRequest().body(String.valueOf(authenticationResponse));
        }
        return  ResponseEntity.ok().body(String.valueOf(authenticationResponse));

    }

    private void saveUserToken(UserProfile userProfile, String jwtToken) {
        var token= new Token(
                jwtToken,
                TokenType.BEARER,
                false,
                false,
                userProfile.getId()
        );
        tokenRepo.insert(token);
    }


    public ResponseEntity<String> authenticate(AutheticationRequest request) {
        AtomicReference<String> response= new AtomicReference<>("Wrong Password!");

        var user = authRepo.findByEmail(request.getEmail());
        user.ifPresentOrElse(
                (s) -> {
                    var a=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
                     if (a.isAuthenticated()){
                         s.setUserStatut(UserStatut.ONLINE);
                         revokeAllUserTokens(s.getId());
                         authRepo.save(s);
                         var jwtToken =jwtService.generateToken(s);
                         saveUserToken(s,jwtToken);

                         response.set(jwtToken);
                     }
                },
                () -> {
                   response.set("User not found");

                }
        );
        if(String.valueOf(response).equals("User not found") && String.valueOf(response).equals("Wrong Password!")){
            return ResponseEntity.badRequest().body(String.valueOf(response));
        }

        return  ResponseEntity.ok().body(String.valueOf(response));

    }

    //TODO REVIEW REVOKE TOKEN
    private void revokeAllUserTokens(String userID){
        var validTokens= tokenRepo.findAllValidTokensByUserID(userID);
        if(validTokens.isEmpty())
            return;
        validTokens.forEach(t->{
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepo.saveAll(validTokens);
    }


    public ResponseEntity<String> checkTokenValidityAndConnect(String token){
        AtomicBoolean isValid= new AtomicBoolean(true);
        UserProfile user=findProfileByToken(token);
        System.out.println("00");

        var isTokenValid=tokenRepo.findByToken(token.substring(7))
                .map(t-> !t.isExpired() && !t.isRevoked())
                .orElse(false);
        System.out.println(isTokenValid);
        if (!isTokenValid) {
            return ResponseEntity.badRequest().body("Your Session Has Expired");
        }else {
            revokeAllUserTokens(user.getId());
            var jwtToken =jwtService.generateToken(user);
            saveUserToken(user,jwtToken);
            return ResponseEntity.ok().body(jwtToken);
        }
    }


    public UserProfile findProfileByToken(String token){
        var jwtToken=token.substring(7);
        var email=jwtService.extractUserName(jwtToken);
        return authRepo.findByEmail(email).orElseThrow();
    }

}
