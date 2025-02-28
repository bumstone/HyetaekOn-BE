package com.hyetaekon.hyetaekon.common.publicdata.controller;


import com.hyetaekon.hyetaekon.common.publicdata.dto.*;
import com.hyetaekon.hyetaekon.common.publicdata.service.PublicServiceDataServiceImpl;
import com.hyetaekon.hyetaekon.common.publicdata.util.PublicServiceDataValidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hyetaekon.hyetaekon.common.publicdata.util.PublicDataPath.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/public-data")
@RequiredArgsConstructor
public class PublicServiceDataController {

  private final PublicServiceDataServiceImpl publicServiceDataService;
  private final PublicServiceDataValidate validator;

  /**
   * 공공서비스 목록 전체 조회 및 Batch Insert 저장
   */
  @PostMapping("/serviceList")
  public ResponseEntity<List<PublicServiceDataDto.Data>> createAndStoreServiceList() {

    List<PublicServiceDataDto.Data> response = validator.validateAndHandleException(() -> {
      // 공공데이터 API에서 전체 서비스 목록 조회
      List<PublicServiceDataDto> dtoList = publicServiceDataService.fetchPublicServiceData(SERVICE_LIST);

      // 조회된 데이터 처리 및 저장 (Batch Insert)
      return publicServiceDataService.upsertServiceData(dtoList);
    }, SERVICE_LIST);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 공공서비스 상세정보 전체 조회 및 Batch Insert 저장
   */
  @PostMapping("/serviceDetailList")
  public ResponseEntity<List<PublicServiceDetailDataDto.Data>> createAndStoreServiceDetailList() {

    List<PublicServiceDetailDataDto.Data> response = validator.validateAndHandleException(
        () -> {
          // 공공데이터 API에서 전체 상세정보 조회
          List<PublicServiceDetailDataDto> dtoList = publicServiceDataService.fetchPublicServiceDetailData(
              SERVICE_DETAIL_LIST);

          // 조회된 데이터 처리 및 저장 (Batch Insert)
          return publicServiceDataService.upsertServiceDetailData(dtoList);
        },
        SERVICE_DETAIL_LIST
    );

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  /**
   * 공공서비스 지원조건 전체 조회 및 Batch Insert 저장
   */
  @PostMapping("/supportConditionsList")
  public ResponseEntity<List<PublicServiceConditionsDataDto.Data>> createAndStoreSupportConditionsList() {

    List<PublicServiceConditionsDataDto.Data> response = validator.validateAndHandleException(() -> {
      // 공공데이터 API에서 전체 지원조건 데이터 조회
      List<PublicServiceConditionsDataDto> dtoList = publicServiceDataService.fetchPublicServiceConditionsData(
          SERVICE_CONDITIONS_LIST);

      // 조회된 데이터 처리 및 저장 (Batch Insert)
      return publicServiceDataService.upsertSupportConditionsData(dtoList);
    }, SERVICE_CONDITIONS_LIST);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
