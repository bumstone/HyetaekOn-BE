package com.hyetaekon.hyetaekon.answer.mapper;

import com.hyetaekon.hyetaekon.answer.dto.AnswerDto;
import com.hyetaekon.hyetaekon.answer.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnswerMapper {
    @Mapping(target = "content", expression = "java(answer.getDisplayContent())")
    AnswerDto toDto(Answer answer);

    Answer toEntity(AnswerDto answerDto);
}
