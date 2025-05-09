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
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Comment toEntity(CommentCreateRequestDto requestDto);

    @Mapping(source = "user.nickname", target = "nickName")
    @Mapping(source = "post.id", target = "postId")
    CommentListResponseDto toResponseDto(Comment comment);

    default String getDisplayContent(Comment comment) {
        if (comment.getDeletedAt() != null) {
            return "삭제된 댓글입니다.";
        } else if (comment.getSuspendAt() != null) {
            return "관리자에 의해 정지된 댓글입니다.";
        }
        return comment.getContent();
    }
}