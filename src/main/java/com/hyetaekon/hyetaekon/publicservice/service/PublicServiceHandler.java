package com.hyetaekon.hyetaekon.publicservice.service;

import com.hyetaekon.hyetaekon.publicservice.dto.PublicServiceDetailResponseDto;
import com.hyetaekon.hyetaekon.publicservice.dto.PublicServiceListResponseDto;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.publicservice.entity.ServiceCategory;
import com.hyetaekon.hyetaekon.publicservice.mapper.PublicServiceMapper;
import com.hyetaekon.hyetaekon.publicservice.repository.PublicServiceRepository;
import com.hyetaekon.hyetaekon.publicservice.util.PublicServiceValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicServiceHandler {
    private final PublicServiceRepository publicServiceRepository;
    private final PublicServiceMapper publicServiceMapper;
    private final PublicServiceValidate publicServiceValidate;


    public Page<PublicServiceListResponseDto> getServicesByCategory(ServiceCategory category, Pageable pageable) {
        Page<PublicService> services = publicServiceRepository.findByServiceCategory(category, pageable);
        return services.map(publicServiceMapper::toListDto);
    }

    public PublicServiceDetailResponseDto getServiceDetail(Long serviceId) {
        PublicService service = publicServiceValidate.validateServiceById(serviceId);

        // 조회수 증가
        service.updateViewsUp();
        publicServiceRepository.save(service);

        return publicServiceMapper.toDetailDto(service);
    }

    public List<PublicServiceListResponseDto> getPopularServices() {
        // publicServiceValidate.validateUserById(userId);

        List<PublicService> services = publicServiceRepository.findTop6ByOrderByViewsDesc();

        return services.stream()
            .map(publicServiceMapper::toListDto)
            .collect(Collectors.toList());
    }

    public ServiceCategory getServiceCategory(String categoryName) {
        return publicServiceValidate.validateServiceCategory(categoryName);
    }
}
