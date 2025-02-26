package com.hyetaekon.hyetaekon.common.publicdata.controller;


import com.hyetaekon.hyetaekon.common.publicdata.dto.PublicServiceConditionsDataParsedResponseDto;
import com.hyetaekon.hyetaekon.common.publicdata.dto.PublicServiceDataParsedResponseDto;
import com.hyetaekon.hyetaekon.common.publicdata.dto.PublicServiceDetailDataParsedResponseDto;
import com.hyetaekon.hyetaekon.common.publicdata.service.PublicServiceDataService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class PublicServiceDataController {

  private final PublicServiceDataService publicServiceDataService;

  @PostMapping("/serviceList")
  public ResponseEntity<List<PublicServiceDataParsedResponseDto>> getServiceList(
      @RequestParam(name = "page") @Min(0) int page,
      @RequestParam(name = "perPage") @Positive int perPage) throws URISyntaxException {

  }


  @PostMapping("/serviceDetailList")
  public ResponseEntity<List<PublicServiceDetailDataParsedResponseDto>> getServiceDetailList(
          @RequestParam(name = "page") @Min(0) int page,
          @RequestParam(name = "perPage") @Positive int perPage) throws URISyntaxException {

  }

  @PostMapping("/supportConditions")
  public ResponseEntity<List<PublicServiceConditionsDataParsedResponseDto>> getSupportConditionsList(
          @RequestParam(name = "page") @Min(0) int page,
          @RequestParam(name = "perPage") @Positive int perPage) throws URISyntaxException {


  }


}
