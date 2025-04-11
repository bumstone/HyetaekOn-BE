package com.hyetaekon.hyetaekon.post.mapper;

import com.hyetaekon.hyetaekon.post.dto.PostDto;
import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.post.entity.PostImage;
import com.hyetaekon.hyetaekon.post.entity.PostType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "imageUrls", expression = "java(post.getPostImages() != null ? post.getPostImages().stream().map(PostImage::getImageUrl).collect(Collectors.toList()) : null)")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "publicServiceId", source = "publicServiceId")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "deletedAt", source = "deletedAt")
    @Mapping(target = "postType", source = "postType")
    @Mapping(target = "serviceUrl", source = "serviceUrl")
    @Mapping(target = "recommendCnt", source = "recommendCnt")
    @Mapping(target = "viewCount", source = "viewCount")
    @Mapping(target = "urlTitle", source = "urlTitle")
    @Mapping(target = "urlPath", source = "urlPath")
    @Mapping(target = "tags", source = "tags")
    @Mapping(target = "categoryId", source = "categoryId")
    PostDto toDto(Post post);

    default Post toEntity(PostDto dto) {
        Post post = new Post();
        post.setUserId(dto.getUserId());
        post.setPublicServiceId(dto.getPublicServiceId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setPostType(PostType.valueOf(dto.getPostType()));
        post.setServiceUrl(dto.getServiceUrl());
        post.setUrlTitle(dto.getUrlTitle());
        post.setUrlPath(dto.getUrlPath());
        post.setTags(dto.getTags());
        post.setCategoryId(dto.getCategoryId());

        // 이미지 URL -> PostImage 객체로 변환
        if (dto.getImageUrls() != null) {
            List<PostImage> images = dto.getImageUrls().stream()
                    .map(url -> {
                        PostImage img = new PostImage();
                        img.setImageUrl(url);
                        img.setPost(post); // 양방향 설정
                        return img;
                    }).collect(Collectors.toList());
            post.setPostImages(images);
        }

        return post;
    }
}
