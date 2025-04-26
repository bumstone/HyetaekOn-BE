package com.hyetaekon.hyetaekon.post.mapper;

import com.hyetaekon.hyetaekon.post.dto.*;
import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.post.entity.PostImage;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {PostImageMapper.class})
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
    @Mapping(target = "imageUrls", source = "postImages", qualifiedByName = "mapPostImagesToUrls")
    PostDetailResponseDto toPostDetailDto(Post post);

    // PostCreateRequestDto -> Post 변환 (새 게시글 생성)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Post toEntity(PostCreateRequestDto createDto);

    // null 아닌 값만 업데이트
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostFromDto(PostUpdateRequestDto updateDto, @MappingTarget Post post);

    // 이미지만 URL 리스트로 변환 (soft delete 처리된 것 제외)
    @Named("mapPostImagesToUrls")
    default List<String> mapPostImagesToUrls(List<PostImage> postImages) {
        if (postImages == null) {
            return Collections.emptyList();
        }
        return postImages.stream()
            .filter(img -> img.getDeletedAt() == null)
            .map(PostImage::getImageUrl)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
