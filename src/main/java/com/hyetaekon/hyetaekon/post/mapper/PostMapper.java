package com.hyetaekon.hyetaekon.post.mapper;

import com.hyetaekon.hyetaekon.post.dto.*;
import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.post.entity.PostImage;
import com.hyetaekon.hyetaekon.post.entity.PostType;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.user.entity.User;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    // Post -> PostListResponseDto 변환
    @Mapping(source = "id", target = "postId")
    @Mapping(source = "user.nickname", target = "nickName")
    @Mapping(source = "postType.koreanName", target = "postType")
    PostListResponseDto toPostListDto(Post post);

    // Post -> PostDetailResponseDto 변환
    @Mapping(source = "id", target = "postId")
    @Mapping(source = "user.nickname", target = "nickName")
    @Mapping(source = "postType.koreanName", target = "postType")
    @Mapping(target = "imageUrls", expression = "java(mapImageUrls(post.getPostImages()))")
    PostDetailReponseDto toPostDetailDto(Post post);

    // PostCreateRequestDto -> Post 변환 (새 게시글 생성)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Post toEntity(PostCreateRequestDto createDto);

    // 이미지 URL 목록 매핑을 위한 기본 메서드
    default List<String> mapImageUrls(List<com.hyetaekon.hyetaekon.post.entity.PostImage> postImages) {
        if (postImages == null) {
            return java.util.Collections.emptyList();
        }
        return postImages.stream()
            .map(com.hyetaekon.hyetaekon.post.entity.PostImage::getImageUrl)
            .filter(java.util.Objects::nonNull)
            .collect(java.util.stream.Collectors.toList());
    }

    // PostUpdateRequestDto로 Post 업데이트
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostFromDto(PostUpdateRequestDto updateDto, @MappingTarget Post post);

    default List<String> mapPostImages(Post post) {
        if (post.getPostImages() == null) return null;
        return post.getPostImages().stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());
    }
}
