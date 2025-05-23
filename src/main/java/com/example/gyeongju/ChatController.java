package com.example.gyeongju;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatClient chatClient;

    @GetMapping("/chat")
    public void chat() {
        chatClient.prompt()
                .user("hello")
                .call()
                .content();
    }
}
