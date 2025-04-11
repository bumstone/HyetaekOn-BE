package com.hyetaekon.hyetaekon.banner.service;

import com.hyetaekon.hyetaekon.banner.dto.BannerDto;
import com.hyetaekon.hyetaekon.banner.entity.Banner;
import com.hyetaekon.hyetaekon.banner.mapper.BannerMapper;
import com.hyetaekon.hyetaekon.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;

    // 배너 목록 조회 (모든 사용자)
    public List<BannerDto> getBanners() {
        List<Banner> banners = bannerRepository.findAllByOrderByDisplayOrderAsc();
        return banners.stream().map(BannerMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    // 배너 상세 조회 (모든 사용자)
    public BannerDto getBanner(Long bannerId) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배너가 존재하지 않습니다."));
        return BannerMapper.INSTANCE.toDto(banner);
    }

    // 배너 목록 조회 (관리자)
    public List<BannerDto> getAdminBanners() {
        return getBanners();
    }

    // 배너 등록 (관리자)
    public BannerDto createBanner(BannerDto bannerDto) {
        Banner banner = BannerMapper.INSTANCE.toEntity(bannerDto);
        banner.setCreatedAt(LocalDateTime.now());
        banner.setUpdatedAt(LocalDateTime.now());
        return BannerMapper.INSTANCE.toDto(bannerRepository.save(banner));
    }

    // 배너 수정 (관리자)
    public BannerDto updateBanner(Long bannerId, BannerDto bannerDto) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배너가 존재하지 않습니다."));

        banner.setTitle(bannerDto.getTitle());
        banner.setImageUrl(bannerDto.getImageUrl());
        banner.setLinkUrl(bannerDto.getLinkUrl());
        banner.setDisplayOrder(bannerDto.getDisplayOrder());
        banner.setUpdatedAt(LocalDateTime.now());

        return BannerMapper.INSTANCE.toDto(bannerRepository.save(banner));
    }

    // 배너 삭제 (관리자)
    public void deleteBanner(Long bannerId) {
        bannerRepository.deleteById(bannerId);
    }

    // 배너 순서 변경 (관리자)
    @Transactional
    public void updateBannerOrder(List<Long> bannerIds) {
        int order = 1;
        for (Long bannerId : bannerIds) {
            Banner banner = bannerRepository.findById(bannerId)
                    .orElseThrow(() -> new IllegalArgumentException("배너를 찾을 수 없습니다."));
            banner.setDisplayOrder(order++);
        }
    }
}
