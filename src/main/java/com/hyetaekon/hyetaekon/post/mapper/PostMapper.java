package com.hyetaekon.hyetaekon.post.mapper;

import com.hyetaekon.hyetaekon.post.dto.PostDto;
import com.hyetaekon.hyetaekon.post.entity.Post;
import com.hyetaekon.hyetaekon.post.entity.PostImage;
import com.hyetaekon.hyetaekon.post.entity.PostType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "imageUrls", expression = "java(mapPostImages(post))")
    @Mapping(target = "postType", expression = "java(post.getPostType() != null ? post.getPostType().name() : null)")
    PostDto toDto(Post post);

    default Post toEntity(PostDto dto) {
        Post post = new Post();
        post.setUserId(dto.getUserId());
        post.setPublicServiceId(dto.getPublicServiceId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setPostType(dto.getPostType() != null ? PostType.valueOf(dto.getPostType()) : null);
        post.setServiceUrl(dto.getServiceUrl());
        post.setUrlTitle(dto.getUrlTitle());
        post.setUrlPath(dto.getUrlPath());
        post.setTags(dto.getTags());
        post.setCategoryId(dto.getCategoryId());

        if (dto.getImageUrls() != null) {
            List<PostImage> images = dto.getImageUrls().stream()
                    .map(url -> {
                        PostImage img = new PostImage();
                        img.setImageUrl(url);
                        img.setPost(post);
                        return img;
                    }).collect(Collectors.toList());
            post.setPostImages(images);
        }

        return post;
    }

    default List<String> mapPostImages(Post post) {
        if (post.getPostImages() == null) return null;
        return post.getPostImages().stream()
                .map(PostImage::getImageUrl)
                .collect(Collectors.toList());
    }
}
