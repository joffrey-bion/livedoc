package org.example.shelf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // prefixes for all subscriptions
        config.enableSimpleBroker("/queue", "/topic");
        config.setUserDestinationPrefix("/user");

        // /app for normal calls, /topic for subscription events
        config.setApplicationDestinationPrefixes("/app", "/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/sample-ws-endpoint")
                .setAllowedOrigins("http://localhost:3000") // to allow frontend server proxy requests in dev mode
                .withSockJS();
    }
}
