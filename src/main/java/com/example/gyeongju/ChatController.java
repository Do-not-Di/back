package com.example.gyeongju;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatClient chatClient;
    private final OpenAiImageModel imageModel;
    @Value("classpath:/interview.md")
    private Resource resource;
    @Value("classpath:/example.md")
    private Resource example;

    @GetMapping("/api/profile")
    public UserInfo profile(@RequestParam String input) throws IOException {
//        UserInfo userInfo = userInfo(example.getContentAsString(StandardCharsets.UTF_8));
        UserInfo userInfo = userInfo(input);
        System.out.println(userInfo);
        return userInfo;
    }

    private UserInfo userInfo(String input) throws IOException {
        return chatClient.prompt(resource.getContentAsString(StandardCharsets.UTF_8))
                .user(input)
                .call()
                .entity(UserInfo.class);
    }

    @Getter
    @ToString
    static class UserInfo {
        private String host;
        private String house;
        private String around;
        private boolean wifi;
        private boolean parkingLot;
        private boolean cook;
    }

    @GetMapping("/api/image")
    public String image() throws IOException {
        return image(resource.getContentAsString(StandardCharsets.UTF_8));
    }

    private String image(String input) {
        OpenAiImageOptions options = OpenAiImageOptions.builder()
                .quality("high")
                .model("gpt-image-1")
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
