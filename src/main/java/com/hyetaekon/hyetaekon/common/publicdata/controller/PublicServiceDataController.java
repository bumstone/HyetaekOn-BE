package com.hyetaekon.hyetaekon.common.publicdata.controller;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.hyetaekon.hyetaekon.common.exception.ErrorCode;
import com.hyetaekon.hyetaekon.common.exception.GlobalException;
import com.hyetaekon.hyetaekon.common.publicdata.dto.*;
import com.hyetaekon.hyetaekon.common.publicdata.service.PublicServiceDataService;
import com.hyetaekon.hyetaekon.common.publicdata.util.PublicDataPath;
import com.hyetaekon.hyetaekon.common.publicdata.util.PublicServiceDataValidate;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/public-data")
@RequiredArgsConstructor
public class PublicServiceDataController {

  private final PublicServiceDataService publicServiceDataService;

  private final PublicServiceDataValidate validator;

  /**
   * 공공서비스 목록 조회 및 저장
   */
  @PostMapping("/serviceList")
  public ResponseEntity<List<PublicServiceDataParsedResponseDto>> getServiceList(
      @RequestParam(name = "page") @PositiveOrZero int page,
      @RequestParam(name = "perPage") @Positive int perPage) {

    List<PublicServiceDataParsedResponseDto> responseList = validator.validateAndHandleException(() -> {
      // 공공데이터 API에서 서비스 목록 조회
      PublicServiceDataDto publicServiceDataDto = publicServiceDataService.fetchPublicServiceData(
          PublicDataPath.SERVICE_LIST,
          "page", String.valueOf(page),
          "perPage", String.valueOf(perPage));

      // 조회된 데이터 처리 및 저장
      return publicServiceDataService.upsertServiceData(publicServiceDataDto);
    }, PublicDataPath.SERVICE_LIST);

    return ResponseEntity.status(HttpStatus.OK).body(responseList);
  }

  /**
   * 공공서비스 상세정보 조회 및 저장
   */
  @PostMapping("/serviceDetailList")
  public ResponseEntity<List<PublicServiceDetailDataParsedResponseDto>> getServiceDetailList(
      @RequestParam(name = "page") @PositiveOrZero int page,
      @RequestParam(name = "perPage") @Positive int perPage) {

    List<PublicServiceDetailDataParsedResponseDto> responseList = validator.validateAndHandleException(() -> {
      // 공공데이터 API에서 서비스 상세 정보 조회
      PublicServiceDetailDataDto publicServiceDetailDataDto = publicServiceDataService.fetchPublicServiceDetailData(
          PublicDataPath.SERVICE_DETAIL_LIST,
          "page", String.valueOf(page),
          "perPage", String.valueOf(perPage));

      // 조회된 데이터 처리 및 저장
      return publicServiceDataService.upsertServiceDetailData(publicServiceDetailDataDto);
    }, PublicDataPath.SERVICE_DETAIL_LIST);

    return ResponseEntity.status(HttpStatus.OK).body(responseList);
  }

  /**
   * 공공서비스 지원조건 조회 및 저장
   */
  @PostMapping("/supportConditions")
  public ResponseEntity<List<PublicServiceConditionsDataParsedResponseDto>> getSupportConditionsList(
      @RequestParam(name = "page") @PositiveOrZero int page,
      @RequestParam(name = "perPage") @Positive int perPage) {

    List<PublicServiceConditionsDataParsedResponseDto> responseList = validator.validateAndHandleException(() -> {
      // 공공데이터 API에서 지원조건 정보 조회
      PublicServiceConditionsDataDto publicServiceConditionsDataDto = publicServiceDataService.fetchPublicServiceConditionsData(
          PublicDataPath.SERVICE_CONDITIONS_LIST,
          "page", String.valueOf(page),
          "perPage", String.valueOf(perPage));


      return publicServiceDataService.upsertSupportConditionsData(publicServiceConditionsDataDto);
    }, PublicDataPath.SERVICE_CONDITIONS_LIST);

    if (responseList == null || responseList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    return ResponseEntity.status(HttpStatus.OK).body(responseList);
  }
}
