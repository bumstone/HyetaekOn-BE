package com.hyetaekon.hyetaekon.answer.mapper;

import com.hyetaekon.hyetaekon.answer.dto.AnswerDto;
import com.hyetaekon.hyetaekon.answer.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

    AnswerDto toDto(Answer answer);

    @Mapping(target = "id", ignore = true)  // id는 자동 생성되므로 무시
    @Mapping(target = "selected", ignore = true)  // 기본값 false 처리
    @Mapping(target = "createdAt", ignore = true) // createdAt 자동 설정
    Answer toEntity(AnswerDto answerDto);
}
