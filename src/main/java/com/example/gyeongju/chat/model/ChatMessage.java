package com.example.gyeongju.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private String senderId;
    private String receiverId;
    private String message;

}
