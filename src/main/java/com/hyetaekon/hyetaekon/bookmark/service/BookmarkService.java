package com.hyetaekon.hyetaekon.bookmark.service;

import com.hyetaekon.hyetaekon.bookmark.entity.Bookmark;
import com.hyetaekon.hyetaekon.bookmark.repository.BookmarkRepository;
import com.hyetaekon.hyetaekon.common.exception.GlobalException;
import com.hyetaekon.hyetaekon.publicservice.entity.PublicService;
import com.hyetaekon.hyetaekon.publicservice.repository.PublicServiceRepository;
import com.hyetaekon.hyetaekon.publicservice.service.mongodb.ServiceMatchedHandler;
import com.hyetaekon.hyetaekon.user.entity.User;
import com.hyetaekon.hyetaekon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.hyetaekon.hyetaekon.common.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final PublicServiceRepository publicServiceRepository;
    private final ServiceMatchedHandler serviceMatchedHandler;

    public void addBookmark(String serviceId, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new GlobalException(BOOKMARK_USER_NOT_FOUND));

        PublicService publicService = publicServiceRepository.findById(serviceId)
            .orElseThrow(() -> new GlobalException(SERVICE_NOT_FOUND_BY_ID));

        // 이미 북마크가 있는지 확인
        if (bookmarkRepository.existsByUserIdAndPublicServiceId(userId, serviceId)) {
            throw new GlobalException(BOOKMARK_ALREADY_EXISTS);
        }

        Bookmark bookmark = Bookmark.builder()
            .user(user)
            .publicService(publicService)
            .build();

        bookmarkRepository.save(bookmark);

        // 북마크 수 증가
        publicService.increaseBookmarkCount();
        publicServiceRepository.save(publicService);
        // 지연된 캐시 무효화 추가
        delayedCacheInvalidation(userId);
    }

    @Transactional
    public void removeBookmark(String serviceId, Long userId) {
        Bookmark bookmark = bookmarkRepository.findByUserIdAndPublicServiceId(userId, serviceId)
            .orElseThrow(() -> new GlobalException(BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);

        // 북마크 수 감소
        PublicService publicService = bookmark.getPublicService();
        publicService.decreaseBookmarkCount();
        publicServiceRepository.save(publicService);
        // 지연된 캐시 무효화 추가
        delayedCacheInvalidation(userId);
    }

    @Async("applicationTaskExecutor") // 사용할 TaskExecutor 지정
    protected void delayedCacheInvalidation(Long userId) {
        try {
            Thread.sleep(2000);
            serviceMatchedHandler.refreshMatchedServicesCache(userId);
            log.debug("사용자 {} 북마크 변경으로 인한 지연된 캐시 무효화 완료", userId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("북마크 캐시 무효화 중 인터럽트 발생: {}", userId);
        }
    }

}
