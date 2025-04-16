package com.hyetaekon.hyetaekon.banner.mapper;

import com.hyetaekon.hyetaekon.banner.dto.BannerDto;
import com.hyetaekon.hyetaekon.banner.entity.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    BannerMapper INSTANCE = Mappers.getMapper(BannerMapper.class);

    BannerDto toDto(Banner banner);

    @Mapping(target = "id", ignore = true)  // id는 자동 생성됨
    Banner toEntity(BannerDto bannerDto);
}
