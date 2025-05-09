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

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.nickname", target = "nickname")
    Answer toEntity(AnswerDto answerDto);
}
