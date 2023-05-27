package com.example.jwtSecurity.config;

import com.example.jwtSecurity.models.UserStatut;
import com.example.jwtSecurity.services.AuthentificationRepository;
import com.example.jwtSecurity.services.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final AuthentificationRepository repo;
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        final JwtService jwtService = new JwtService();
        final String authHeader = request.getHeader("Authorization");

        final String userEmail;
        final String jwtToken;
        if(authHeader==null || !authHeader.startsWith("Bearer")){
            return;
        }
        jwtToken= authHeader.substring(7);
        var storedToken=tokenRepository.findByToken(jwtToken)
                .orElse(null);
        if(storedToken!=null){
            //storedToken.setExpired(true);
            //storedToken.setRevoked(true);
            //tokenRepository.save(storedToken);

            userEmail= jwtService.extractUserName(jwtToken);
            var userProfile=repo.findByEmail(userEmail).orElseThrow();
            userProfile.setUserStatut(UserStatut.OFFLINE);
            repo.save(userProfile);
            tokenRepository.delete(storedToken);
        }

    }
}
