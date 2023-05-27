package com.example.messageServices.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig2 implements WebSocketConfigurer {

    private final static String CHAT_ENDPOINT="/publicChat";

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(getWebSocketHandler(),CHAT_ENDPOINT)
                .setAllowedOrigins("*");
    }

    @Bean
    public ChatWebSocketHandler getWebSocketHandler(){
        return new ChatWebSocketHandler();
    }
}
