package com.hyetaekon.hyetaekon.publicservice.controller;

import com.hyetaekon.hyetaekon.publicservice.dto.PublicServiceDetailResponseDto;
import com.hyetaekon.hyetaekon.publicservice.dto.PublicServiceListResponseDto;
import com.hyetaekon.hyetaekon.publicservice.entity.ServiceCategory;
import com.hyetaekon.hyetaekon.publicservice.service.PublicServiceHandler;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class PublicServiceController {
    private final PublicServiceHandler publicServiceHandler;
    // private final AuthenticateUser authenticateUser;


    // 서비스 분야별 공공서비스 목록 조회
    @GetMapping("/{category}")
    public ResponseEntity<Page<PublicServiceListResponseDto>> getServicesByCategory (
        @PathVariable("category") String categoryName,
        @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
        @RequestParam(name = "size", defaultValue = "9") @Positive @Max(30) int size) {

        ServiceCategory category = publicServiceHandler.getServiceCategory(categoryName);
        return ResponseEntity.ok(publicServiceHandler.getServicesByCategory(category, PageRequest.of(page, size)));

    }

    // 공공서비스 상세 조회
    @GetMapping("/{serviceId}")
    public ResponseEntity<PublicServiceDetailResponseDto> getServiceDetail (@PathVariable("serviceId") Long serviceId) {
        return ResponseEntity.ok(publicServiceHandler.getServiceDetail(serviceId));
    }

    // 인기 서비스 목록 조회(조회수) -> 6개 리스트 고정
    // TODO: 유저 인증
    @GetMapping("/popular")
    public ResponseEntity<List<PublicServiceListResponseDto>> getPopolarServices() {
        return ResponseEntity.ok(publicServiceHandler.getPopularServices()); // 최대 6개로 제한
    }

}
