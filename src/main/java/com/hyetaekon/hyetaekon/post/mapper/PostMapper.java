package com.hyetaekon.hyetaekon.post.mapper;

import com.hyetaekon.hyetaekon.post.dto.PostDto;
import com.hyetaekon.hyetaekon.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    PostDto toDto(Post post);
    Post toEntity(PostDto postDto);
}