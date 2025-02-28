package com.hyetaekon.hyetaekon.common.publicdata.service;

import com.hyetaekon.hyetaekon.common.publicdata.dto.*;
import com.hyetaekon.hyetaekon.common.publicdata.util.PublicDataPath;

import java.util.List;

public interface PublicServiceDataService {

    /**
     * 공공서비스 전체 목록 API에서 데이터를 조회합니다.
     */
    List<PublicServiceDataDto> fetchPublicServiceData(PublicDataPath apiPath);

    /**
     * 공공서비스 전체 상세정보 API에서 데이터를 조회합니다.
     */
    List<PublicServiceDetailDataDto> fetchPublicServiceDetailData(PublicDataPath apiPath);

    /**
     * 공공서비스 지원조건 전체 API에서 데이터를 조회합니다.
     */
    List<PublicServiceConditionsDataDto> fetchPublicServiceConditionsData(PublicDataPath apiPath);

    /**
     * 조회된 공공서비스 목록 데이터를 Batch Insert 저장합니다.
     */
    List<PublicServiceDataDto.Data> upsertServiceData(List<PublicServiceDataDto> publicServiceDataDto);

    /**
     * 조회된 공공서비스 상세정보 데이터를 Batch Insert 저장합니다.
     */
    List<PublicServiceDetailDataDto.Data> upsertServiceDetailData(List<PublicServiceDetailDataDto> publicServiceDetailDataDtoList);

    /**
     * 조회된 공공서비스 지원조건 데이터를 Batch Insert 저장합니다.
     */
    List<PublicServiceConditionsDataDto.Data> upsertSupportConditionsData(List<PublicServiceConditionsDataDto> publicServiceConditionsDataDto);
}