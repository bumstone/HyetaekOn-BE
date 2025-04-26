package com.hyetaekon.hyetaekon.post.mapper;

import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.post.entity.PostImage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostImageMapper {

    // 게시글 이미지 변환
    default PostImage toPostImage(String url, Post post) {
        if (url == null || post == null) {
            throw new IllegalArgumentException("url 또는 post가 null일 수 없습니다.");
        }

        return PostImage.builder()
            .imageUrl(url)
            .post(post)
            .build();
    }

    // URL 리스트로 PostImage 리스트 생성
    default List<PostImage> toEntityList(List<String> uploadedUrls, Post post) {
        if (uploadedUrls == null || uploadedUrls.isEmpty()) {
            return Collections.emptyList();
        }
        return uploadedUrls.stream()
            .map(url -> toPostImage(url, post))
            .collect(Collectors.toList());
    }

}
