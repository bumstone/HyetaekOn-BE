package com.hyetaekon.hyetaekon.answer.service;

import com.hyetaekon.hyetaekon.answer.dto.AnswerDto;
import com.hyetaekon.hyetaekon.answer.entity.Answer;
import com.hyetaekon.hyetaekon.answer.mapper.AnswerMapper;
import com.hyetaekon.hyetaekon.answer.repository.AnswerRepository;
import com.hyetaekon.hyetaekon.common.exception.ErrorCode;
import com.hyetaekon.hyetaekon.common.exception.GlobalException;
import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.post.repository.PostRepository;
import com.hyetaekon.hyetaekon.user.entity.PointActionType;
import com.hyetaekon.hyetaekon.user.service.UserPointService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final PostRepository postRepository;
    private final UserPointService userPointService;

    // 게시글에 답변 목록 조회
    public Page<AnswerDto> getAnswersByPostId(Long postId, Pageable pageable) {
        // 게시글 존재 여부 확인
        postRepository.findByIdAndDeletedAtIsNull(postId)
            .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND_BY_ID));

        // 채택된 답변이 먼저 나오고, 그 다음 최신순으로 정렬
        Page<Answer> answersPage = answerRepository.findByPostIdOrderBySelectedDescCreatedAtDesc(postId, pageable);

        return answersPage.map(answerMapper::toDto);
    }

    public AnswerDto createAnswer(Long postId, AnswerDto answerDto, Long userId) {
        // 게시글 존재 여부 확인
        postRepository.findByIdAndDeletedAtIsNull(postId)
            .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND_BY_ID));

        Answer answer = answerMapper.toEntity(answerDto);
        answer.setPostId(postId);
        answer.setUserId(userId);
        answer.setCreatedAt(LocalDateTime.now());
        answer = answerRepository.save(answer);

        userPointService.addPointForAction(userId, PointActionType.ANSWER_CREATION);

        return answerMapper.toDto(answer);
    }

    public void selectAnswer(Long postId, Long answerId, Long userId) {
        // 게시글 조회
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId)
            .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND_BY_ID));

        // 요청자가 게시글 작성자인지 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("게시글 작성자만 답변을 채택할 수 있습니다.");
        }

        // 답변 조회
        Answer answer = answerRepository.findById(answerId)
            .orElseThrow(() -> new GlobalException(ErrorCode.ANSWER_NOT_FOUND));

        // 답변이 해당 게시글에 속하는지 확인
        if (!answer.getPostId().equals(postId)) {
            throw new GlobalException(ErrorCode.ANSWER_NOT_MATCHED_POST);
        }

        // 답변 채택 처리
        answer.setSelected(true);
        answerRepository.save(answer);

        // 답변 작성자에게 포인트 부여
        userPointService.addPointForAction(answer.getUserId(), PointActionType.ANSWER_ACCEPTED);
    }

    @Transactional
    public void deleteAnswer(Long postId, Long answerId, Long userId, boolean isAdmin) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new GlobalException(ErrorCode.ANSWER_NOT_FOUND));

        if (!answer.getPostId().equals(postId)) {
            throw new GlobalException(ErrorCode.ANSWER_NOT_MATCHED_POST);
        }

        if (!answer.getUserId().equals(userId) && !isAdmin) {
            throw new AccessDeniedException("답변 삭제 권한이 없습니다.");
        }

        answerRepository.delete(answer);
    }

}
