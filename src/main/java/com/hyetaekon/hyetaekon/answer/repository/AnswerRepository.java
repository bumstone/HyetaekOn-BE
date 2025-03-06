package com.hyetaekon.hyetaekon.answer.repository;

import com.hyetaekon.hyetaekon.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
