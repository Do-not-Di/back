package com.example.gyeongju.chat.handler;

import com.example.gyeongju.chat.model.ChatMessage;
import com.example.gyeongju.chat.service.ChatSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatSessionService chatSessionService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getQueryParam(session, "userId");
        log.info("연결됨: userId={}", userId);
        if (userId != null) {
            chatSessionService.register(userId, session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws Exception {
        ChatMessage chat = objectMapper.readValue(message.getPayload(), ChatMessage.class);
        log.info("Receive message: {}", chat);

        // 메시지 저장
        chatSessionService.saveMessage(chat);

        WebSocketSession receiverSession = chatSessionService.getSession(chat.getReceiverId());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(
                new TextMessage(chat.getSenderId() + ": " + chat.getMessage()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Session Closed");
        chatSessionService.unregister(session);
    }

    // URI 쿼리 파라미터 추출 (Spring 방식)
    private String getQueryParam(WebSocketSession session, String key) {
        if (session.getUri() == null) {
            return null;
        }
        return UriComponentsBuilder.fromUri(session.getUri())
            .build()
            .getQueryParams()
            .getFirst(key);
    }
}
