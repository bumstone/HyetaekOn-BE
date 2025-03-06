package com.hyetaekon.hyetaekon.comment.mapper;

import com.hyetaekon.hyetaekon.comment.dto.CommentDto;
import com.hyetaekon.hyetaekon.comment.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto toDto(Comment comment);

    @Mapping(target = "id", ignore = true)  // id는 자동 생성되므로 무시
    @Mapping(target = "createdAt", ignore = true) // createdAt 자동 설정
    Comment toEntity(CommentDto commentDto);
}
