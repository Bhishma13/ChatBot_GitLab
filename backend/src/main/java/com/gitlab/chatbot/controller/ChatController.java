package com.gitlab.chatbot.controller;

import com.gitlab.chatbot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String chatId = request.getOrDefault("chatId", "default-chat");
        if (message == null || message.trim().isEmpty()) {
            return Map.of("error", "Message cannot be empty");
        }
        
        String response = chatService.chat(message, chatId);
        return Map.of("response", response);
    }
}
