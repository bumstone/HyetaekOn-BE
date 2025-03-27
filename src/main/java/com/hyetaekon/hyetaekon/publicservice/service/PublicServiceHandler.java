package com.hyetaekon.hyetaekon.publicservice.service;

import com.hyetaekon.hyetaekon.bookmark.repository.BookmarkRepository;
import com.hyetaekon.hyetaekon.common.exception.GlobalException;
import com.hyetaekon.hyetaekon.publicservice.dto.PublicServiceDetailResponseDto;
import com.hyetaekon.hyetaekon.publicservice.dto.PublicServiceListResponseDto;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.publicservice.entity.ServiceCategory;
import com.hyetaekon.hyetaekon.publicservice.mapper.PublicServiceMapper;
import com.hyetaekon.hyetaekon.publicservice.repository.PublicServiceRepository;
import com.hyetaekon.hyetaekon.publicservice.util.PublicServiceValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicServiceHandler {
    private final PublicServiceRepository publicServiceRepository;
    private final PublicServiceMapper publicServiceMapper;
    private final PublicServiceValidate publicServiceValidate;
    private final BookmarkRepository bookmarkRepository;

    // 서비스분야별 서비스목록 조회(페이지)
    public Page<PublicServiceListResponseDto> getServicesByCategory(ServiceCategory category, Pageable pageable, Long userId) {
        Page<PublicService> services = publicServiceRepository.findByServiceCategory(category, pageable);

        return services.map(service -> {
            PublicServiceListResponseDto dto = publicServiceMapper.toListDto(service);
            if (userId != 0L) {
                // 로그인한 사용자면 북마크 여부 확인
                dto.setBookmarked(bookmarkRepository.existsByUserIdAndPublicServiceId(userId, service.getId()));
            }
            return dto;
        });
    }

    // 서비스 상세 조회
    @Transactional
    public PublicServiceDetailResponseDto getServiceDetail(Long serviceId, Long userId) {
        PublicService service = publicServiceValidate.validateServiceById(serviceId);

        // 조회수 증가
        service.updateViewsUp();
        publicServiceRepository.save(service);

        PublicServiceDetailResponseDto dto = publicServiceMapper.toDetailDto(service);

        if (userId != 0L) {
            // 로그인한 사용자면 북마크 여부 확인
            dto.setBookmarked(bookmarkRepository.existsByUserIdAndPublicServiceId(userId, service.getId()));
        }

        return dto;
    }

    // 인기 서비스 목록 조회(6개 고정)
    public List<PublicServiceListResponseDto> getPopularServices(Long userId) {

        // 북마크 수 기준으로 상위 6개 서비스 조회
        List<PublicService> services = publicServiceRepository.findTop6ByOrderByBookmarkCntDesc();

        return services.stream()
            .map(service -> {
                PublicServiceListResponseDto dto = publicServiceMapper.toListDto(service);
                // 로그인한 사용자는 북마크 여부 확인
                if (userId == 0L) {
                    dto.setBookmarked(bookmarkRepository.existsByUserIdAndPublicServiceId(userId, service.getId()));
                }
                return dto;
            })
            .collect(Collectors.toList());
    }

    public ServiceCategory getServiceCategory(String categoryName) {
        return publicServiceValidate.validateServiceCategory(categoryName);
    }

  public Page<PublicServiceListResponseDto> getBookmarkedServices(Long userId, Pageable pageable) {
      Page<PublicService> bookmarkedServices = publicServiceRepository.findByBookmarks_User_Id(userId, pageable);

      List<PublicServiceListResponseDto> serviceDtos = bookmarkedServices.getContent().stream()
          .map(service -> {
              PublicServiceListResponseDto dto = publicServiceMapper.toListDto(service);
              dto.setBookmarked(true);
              return dto;
          })
          .collect(Collectors.toList());

      return new PageImpl<>(serviceDtos, pageable, bookmarkedServices.getTotalElements());

  }
}
