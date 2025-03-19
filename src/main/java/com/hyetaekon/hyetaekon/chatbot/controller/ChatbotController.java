package com.hyetaekon.hyetaekon.chatbot.controller;

import com.hyetaekon.hyetaekon.chatbot.dto.ChatbotDto;
import com.hyetaekon.hyetaekon.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {
    private final ChatbotService chatbotService;

    // 📌 사용자가 질문하면 답변을 반환하는 API
    @GetMapping
    public ResponseEntity<ChatbotDto> getAnswer(@RequestParam String question) {
        return ResponseEntity.ok(chatbotService.getAnswer(question));
    }

    // 📌 새로운 질문-답변을 DB에 추가하는 API (관리자용)
    @PostMapping("/add")
    public ResponseEntity<ChatbotDto> addQuestionAndAnswer(@RequestBody ChatbotDto chatbotDto) {
        return ResponseEntity.ok(chatbotService.addQuestionAndAnswer(chatbotDto));
    }
}
