package com.example.gyeongju;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatClient chatClient;
    private final OpenAiImageModel imageModel;

    @GetMapping("/image")
    public String image(@RequestParam String input) {
        OpenAiImageOptions options = OpenAiImageOptions.builder()
                .quality("hd")
                .height(1024)
                .width(1024).build();
        ImagePrompt prompt = new ImagePrompt("""
                    You are professional ai assistant 
                    that supports converting conversation with elderly person 
                    who's explaining about himself into a nice picture that represent him.
                    Following is the conversation.\n
                """ + input, options);
        ImageResponse response = imageModel.call(prompt);
        return resolveImageContent(response);
    }

    private String resolveImageContent(ImageResponse imageResponse) {
        Image image = imageResponse.getResult().getOutput();
        return Optional
                .ofNullable(image.getUrl())
                .orElseGet(image::getB64Json);
    }
}
