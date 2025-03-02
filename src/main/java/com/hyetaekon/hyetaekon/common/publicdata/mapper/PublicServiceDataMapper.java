package com.hyetaekon.hyetaekon.common.publicdata.mapper;

import com.hyetaekon.hyetaekon.common.publicdata.dto.*;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublicServiceDataMapper {
    PublicService updateFromServiceData(PublicService publicService, PublicServiceDataDto.Data data);
    PublicService updateFromDetailData(PublicService publicService, PublicServiceDetailDataDto.Data data);
    PublicService updateFromConditionsData(PublicService publicService, PublicServiceConditionsDataDto.Data data);
}
