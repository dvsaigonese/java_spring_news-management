package com.GoalLineNews.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Cấu hình broker cho client
        config.enableSimpleBroker("/topic"); // Các tin nhắn đến client sẽ có prefix là /topic
        config.setApplicationDestinationPrefixes("/app"); // Client gửi tin nhắn sẽ dùng prefix /app
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Đăng ký endpoint WebSocket với STOMP
        registry
                .addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        SecurityContext context = (SecurityContext) ((ServletServerHttpRequest) request).getServletRequest()
                                .getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
                        if (context != null) {
                            Authentication auth = context.getAuthentication();
                            if (auth != null) {
                                System.out.println("WebSocket Authenticated User: " + auth.getPrincipal());
                            } else {
                                System.out.println("BUGGGGGGGGGGGGGGGG " + auth.getPrincipal());
                            }
                        }
                        return null;
                    }
                })
                .withSockJS(); // Dùng SockJS cho fallback
    }
}
