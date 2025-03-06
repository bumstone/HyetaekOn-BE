package com.hyetaekon.hyetaekon.banner.controller;

import com.hyetaekon.hyetaekon.banner.dto.BannerDto;
import com.hyetaekon.hyetaekon.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    // 🔹 배너 목록 조회 (모든 사용자)
    @GetMapping("/banners")
    public ResponseEntity<List<BannerDto>> getAllBanners() {
        return ResponseEntity.ok(bannerService.getAllBanners());
    }

    // 🔹 배너 상세 조회 (모든 사용자)
    @GetMapping("/banners/{bannerId}")
    public ResponseEntity<BannerDto> getBanner(@PathVariable Long bannerId) {
        return ResponseEntity.ok(bannerService.getBanner(bannerId));
    }

    // 🔹 배너 목록 조회 (관리자 전용)
    @GetMapping("/admin/banners")
    public ResponseEntity<List<BannerDto>> getAdminBanners() {
        return ResponseEntity.ok(bannerService.getAllBanners());
    }

    // 🔹 배너 등록 (관리자 전용)
    @PostMapping("/admin/banners")
    public ResponseEntity<BannerDto> createBanner(@RequestBody BannerDto bannerDto) {
        return ResponseEntity.ok(bannerService.createBanner(bannerDto));
    }

    // 🔹 배너 수정 (관리자 전용)
    @PutMapping("/admin/banners/{bannerId}")
    public ResponseEntity<BannerDto> updateBanner(@PathVariable Long bannerId, @RequestBody BannerDto bannerDto) {
        return ResponseEntity.ok(bannerService.updateBanner(bannerId, bannerDto));
    }

    // 🔹 배너 삭제 (관리자 전용)
    @DeleteMapping("/admin/banners/{bannerId}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long bannerId) {
        bannerService.deleteBanner(bannerId);
        return ResponseEntity.noContent().build();
    }

    // 🔹 배너 순서 변경 (관리자 전용)
    @PatchMapping("/admin/banners")
    public ResponseEntity<Void> updateBannerOrder(@RequestBody List<Long> bannerIds) {
        bannerService.updateBannerOrder(bannerIds);
        return ResponseEntity.noContent().build();
    }
}
