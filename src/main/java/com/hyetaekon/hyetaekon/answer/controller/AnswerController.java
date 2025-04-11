package com.hyetaekon.hyetaekon.answer.controller;

import com.hyetaekon.hyetaekon.answer.dto.AnswerDto;
import com.hyetaekon.hyetaekon.answer.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    // 답변 작성
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AnswerDto> createAnswer(@PathVariable Long postId, @RequestBody AnswerDto answerDto) {
        AnswerDto createdAnswer = answerService.createAnswer(postId, answerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswer);
    }

    // 답변 채택
    @PutMapping("/{answerId}/select")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> selectAnswer(@PathVariable Long answerId) {
        answerService.selectAnswer(answerId);
        return ResponseEntity.ok().build();
    }

    // 답변 삭제 (관리자만 가능)
    @DeleteMapping("/admin/answers/{answerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.noContent().build();
    }
}

