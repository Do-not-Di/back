package com.example.gyeongju.chat.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectedUserWithMessages {

    private String userId;
    private List<ChatMessage> messages;

}
