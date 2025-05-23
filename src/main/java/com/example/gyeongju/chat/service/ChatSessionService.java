package com.example.gyeongju.chat.service;

import com.example.gyeongju.chat.model.ChatMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatSessionService {
    private static final String HOST = "host";

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, List<ChatMessage>> messageHistory = new ConcurrentHashMap<>();

    public void register(String userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    public void unregister(WebSocketSession session) {
        sessions.values().removeIf(s -> s.getId().equals(session.getId()));
    }

    public Set<String> getUsersConnectedToHost() {
        return sessions.keySet()
            .stream()
            .filter(userId -> !HOST.equalsIgnoreCase(userId))
            .collect(Collectors.toSet());
    }

    public void saveMessage(ChatMessage message) {
        String userId = getNonHostId(message);
        messageHistory.computeIfAbsent(userId, k -> new ArrayList<>()).add(message);
    }

    public List<ChatMessage> getMessagesForUser(String userId) {
        return messageHistory.getOrDefault(userId, List.of());
    }

    private String getNonHostId(ChatMessage msg) {
        return HOST.equalsIgnoreCase(msg.getSenderId()) ? msg.getReceiverId()
            : msg.getSenderId();
    }

    public WebSocketSession getSession(String userId) {
        return sessions.get(userId);
    }

}
