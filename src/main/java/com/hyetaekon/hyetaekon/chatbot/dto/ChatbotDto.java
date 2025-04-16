package com.hyetaekon.hyetaekon.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotDto {
    private String question;
    private String answer;
}
