package com.hyetaekon.hyetaekon.answer.repository;

import com.hyetaekon.hyetaekon.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    // 페이지네이션 적용한 답변 목록 조회
    Page<Answer> findByPostId(Long postId, Pageable pageable);

    // 채택 여부 및 등록일 기준 정렬된 페이징 처리된 답변 목록 조회
    Page<Answer> findByPostIdOrderBySelectedDescCreatedAtDesc(Long postId, Pageable pageable);
}
