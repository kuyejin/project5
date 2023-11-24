package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {
    // WebSocket 설정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // "/socket"으로 시작하는 모든 요청을 WebSocket 핸들러로 라우팅
        registry.addEndpoint("/socket");
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/socket/**", buildConfig()); // WebSocket 경로에 대한 CORS 설정 추가
        return source;
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*"); // 모든 origin 허용 (보안상 적절한 값을 설정해야 함)
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");
        return corsConfig;
    }
}
