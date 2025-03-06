package com.hyetaekon.hyetaekon.banner.service;

import com.hyetaekon.hyetaekon.banner.dto.BannerDto;
import com.hyetaekon.hyetaekon.banner.entity.Banner;
import com.hyetaekon.hyetaekon.banner.mapper.BannerMapper;
import com.hyetaekon.hyetaekon.banner.repository.BannerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;

    public List<BannerDto> getAllBanners() {
        return bannerRepository.findAll()
                .stream()
                .map(bannerMapper::toDto)
                .collect(Collectors.toList());
    }

    public BannerDto getBanner(Long bannerId) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found"));
        return bannerMapper.toDto(banner);
    }

    public BannerDto createBanner(BannerDto bannerDto) {
        Banner banner = bannerMapper.toEntity(bannerDto);
        banner = bannerRepository.save(banner);
        return bannerMapper.toDto(banner);
    }

    public BannerDto updateBanner(Long bannerId, BannerDto bannerDto) {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new EntityNotFoundException("Banner not found"));
        banner.setTitle(bannerDto.getTitle());
        banner.setImageUrl(bannerDto.getImageUrl());
        banner.setOrder(bannerDto.getOrder());
        banner = bannerRepository.save(banner);
        return bannerMapper.toDto(banner);
    }

    public void deleteBanner(Long bannerId) {
        bannerRepository.deleteById(bannerId);
    }

    public void updateBannerOrder(List<Long> bannerIds) {
        for (int i = 0; i < bannerIds.size(); i++) {
            Banner banner = bannerRepository.findById(bannerIds.get(i))
                    .orElseThrow(() -> new EntityNotFoundException("Banner not found"));
            banner.setOrder(i);
            bannerRepository.save(banner);
        }
    }
}
