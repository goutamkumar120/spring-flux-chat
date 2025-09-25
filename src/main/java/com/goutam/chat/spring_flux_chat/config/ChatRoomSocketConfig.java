package com.goutam.chat.spring_flux_chat.config;

import com.goutam.chat.spring_flux_chat.service.ChatRoomService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;

import java.util.Map;

@Configuration
public class ChatRoomSocketConfig {

    private final ChatRoomService chatRoomService;

    public ChatRoomSocketConfig(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @Bean
    public HandlerMapping handlerMapping() {
        Map<String, WebSocketHandler> map = Map.of(
                "/chat", chatRoomService
        );
        // Order = -1 ensures this mapping has higher precedence
        return new SimpleUrlHandlerMapping(map, -1);
    }
}

