package com.hyetaekon.hyetaekon.answer.service;

import com.hyetaekon.hyetaekon.answer.dto.AnswerDto;
import com.hyetaekon.hyetaekon.answer.entity.Answer;
import com.hyetaekon.hyetaekon.answer.mapper.AnswerMapper;
import com.hyetaekon.hyetaekon.answer.repository.AnswerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;

    public AnswerDto createAnswer(Long postId, AnswerDto answerDto) {
        Answer answer = answerMapper.toEntity(answerDto);
        answer.setPostId(postId);
        answer = answerRepository.save(answer);
        return answerMapper.toDto(answer);
    }

    public void selectAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found"));
        answer.setSelected(true);
        answerRepository.save(answer);
    }

    public void deleteAnswer(Long answerId) {
        answerRepository.deleteById(answerId);
    }
}
