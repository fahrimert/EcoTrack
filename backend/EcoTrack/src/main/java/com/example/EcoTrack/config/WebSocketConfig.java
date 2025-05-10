package com.example.EcoTrack.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws","/ws-locations") // ← frontend buna bağlanacak
                .setAllowedOriginPatterns("*") // CORS
                .withSockJS(); // SockJS fallback desteği

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // yayılacak veriler
        config.setApplicationDestinationPrefixes("/app"); // frontend'den mesaj alırsan
    }
}