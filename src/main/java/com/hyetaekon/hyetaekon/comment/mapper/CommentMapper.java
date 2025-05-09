package com.hyetaekon.hyetaekon.comment.mapper;

import com.hyetaekon.hyetaekon.comment.dto.CommentCreateRequestDto;
import com.hyetaekon.hyetaekon.comment.dto.CommentDto;
import com.hyetaekon.hyetaekon.comment.dto.CommentListResponseDto;
import com.hyetaekon.hyetaekon.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    Comment toEntity(CommentCreateRequestDto requestDto);

    @Mapping(source = "user.nickname", target = "nickname")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(target = "content", expression = "java(comment.getDisplayContent())")
    CommentListResponseDto toResponseDto(Comment comment);

}