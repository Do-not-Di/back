package com.example.gyeongju.chat.controller;

import com.example.gyeongju.chat.model.ConnectedUserWithMessages;
import com.example.gyeongju.chat.service.ChatSessionService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatInfoController {

    private final ChatSessionService chatSessionService;

    /**
     * 현재 연결된 사용자(userId) 리스트 반환
     */
    @GetMapping("/live")
    public ResponseEntity<List<ConnectedUserWithMessages>> getUsersConnectedToHostD() {
        List<ConnectedUserWithMessages> result = chatSessionService.getUsersConnectedToHost()
            .stream()
            .map(userId -> new ConnectedUserWithMessages(
                userId,
                chatSessionService.getMessagesForUser(userId)
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

}
