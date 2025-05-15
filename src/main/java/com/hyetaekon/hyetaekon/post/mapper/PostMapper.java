package com.hyetaekon.hyetaekon.post.mapper;

import com.hyetaekon.hyetaekon.post.dto.*;
import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.post.entity.PostImage;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    // ✅ 게시글 목록용 DTO 변환
    @Mapping(source = "id", target = "postId")
    @Mapping(source = "user.nickname", target = "nickName")
    @Mapping(source = "postType.koreanName", target = "postType")
    @Mapping(source = "recommendCnt", target = "recommendCnt")
    @Mapping(source = "user.id", target = "userId") // 🔥 추가
    PostListResponseDto toPostListDto(Post post);

    // ✅ 마이페이지용 게시글 DTO
    @Mapping(source = "id", target = "postId")
    @Mapping(source = "user.nickname", target = "nickName")
    @Mapping(target = "content", expression = "java(post.getDisplayContent())")
    MyPostListResponseDto toMyPostListDto(Post post);

    // ✅ 게시글 생성 시 DTO → Entity 변환
    Post toEntity(PostCreateRequestDto createDto);

    // ✅ 게시글 수정 시 일부 값만 업데이트 (null 무시)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostFromDto(PostUpdateRequestDto updateDto, @MappingTarget Post post);

    // ✅ 게시글 상세 보기용 DTO (imageUrls 수동으로 처리)
    // 상세용 DTO (default 메서드 내부에 userId 수동 추가)
    default PostDetailResponseDto toPostDetailDto(Post post) {
        return PostDetailResponseDto.builder()
                .postId(post.getId())
                .userId(post.getUser().getId()) // 🔥 추가
                .nickName(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getDisplayContent())
                .createdAt(post.getCreatedAt())
                .postType(post.getPostType().getKoreanName())
                .recommendCnt(post.getRecommendCnt())
                .viewCnt(post.getViewCnt())
                .urlTitle(post.getUrlTitle())
                .urlPath(post.getUrlPath())
                .tags(post.getTags())
                .imageUrls(
                        post.getPostImages().stream()
                                .filter(img -> img.getDeletedAt() == null)
                                .map(PostImage::getImageUrl)
                                .collect(Collectors.toList())
                )
                .recommended(false)
                .build();
    }

}
