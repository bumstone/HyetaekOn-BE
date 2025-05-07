package com.hyetaekon.hyetaekon.answer.service;

import com.hyetaekon.hyetaekon.answer.dto.AnswerDto;
import com.hyetaekon.hyetaekon.answer.entity.Answer;
import com.hyetaekon.hyetaekon.answer.mapper.AnswerMapper;
import com.hyetaekon.hyetaekon.answer.repository.AnswerRepository;
import com.hyetaekon.hyetaekon.user.entity.PointActionType;
import com.hyetaekon.hyetaekon.user.service.UserPointService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;

    private final UserPointService userPointService;

    public AnswerDto createAnswer(Long postId, AnswerDto answerDto, Long userId) {
        Answer answer = answerMapper.toEntity(answerDto);
        answer.setPostId(postId);
        answer = answerRepository.save(answer);

        userPointService.addPointForAction(userId, PointActionType.ANSWER_CREATION);

        return answerMapper.toDto(answer);
    }

    public void selectAnswer(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found"));
        // 이미 선택된 답변인지 확인
        if (!answer.isSelected()) {
            answer.setSelected(true);
            answerRepository.save(answer);

            // 답변 채택 시 답변 작성자에게 포인트 부여
            userPointService.addPointForAction(answer.getUserId(), PointActionType.ANSWER_ACCEPTED);
        }

        answerRepository.save(answer);
    }

    public void deleteAnswer(Long answerId) {
        answerRepository.deleteById(answerId);
    }
}
