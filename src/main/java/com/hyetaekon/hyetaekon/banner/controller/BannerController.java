package com.hyetaekon.hyetaekon.banner.controller;

import com.hyetaekon.hyetaekon.banner.dto.BannerDto;
import com.hyetaekon.hyetaekon.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    // 배너 목록 조회 (ALL)
    @GetMapping
    public ResponseEntity<List<BannerDto>> getBanners() {
        return ResponseEntity.ok(bannerService.getBanners());
    }

    // 배너 상세 조회 (ALL)
    @GetMapping("/{bannerId}")
    public ResponseEntity<BannerDto> getBanner(@PathVariable Long bannerId) {
        return ResponseEntity.ok(bannerService.getBanner(bannerId));
    }

    // 배너 목록 조회 (ADMIN)
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BannerDto>> getAdminBanners() {
        return ResponseEntity.ok(bannerService.getAdminBanners());
    }

    // 배너 등록 (ADMIN)
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BannerDto> createBanner(@RequestBody BannerDto bannerDto) {
        return ResponseEntity.ok(bannerService.createBanner(bannerDto));
    }

    // 배너 수정 (ADMIN)
    @PutMapping("/admin/{bannerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BannerDto> updateBanner(
            @PathVariable Long bannerId, @RequestBody BannerDto bannerDto) {
        return ResponseEntity.ok(bannerService.updateBanner(bannerId, bannerDto));
    }

    // 배너 삭제 (ADMIN)
    @DeleteMapping("/admin/{bannerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long bannerId) {
        bannerService.deleteBanner(bannerId);
        return ResponseEntity.noContent().build();
    }

    // 배너 순서 변경 (ADMIN)
    @PatchMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateBannerOrder(@RequestBody List<Long> bannerIds) {
        bannerService.updateBannerOrder(bannerIds);
        return ResponseEntity.noContent().build();
    }
}
