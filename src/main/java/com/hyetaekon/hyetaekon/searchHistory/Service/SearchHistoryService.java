package com.hyetaekon.hyetaekon.searchHistory.Service;

import com.hyetaekon.hyetaekon.searchHistory.Dto.SearchHistoryDto;
import com.hyetaekon.hyetaekon.searchHistory.Repository.SearchHistoryRepository;
import com.hyetaekon.hyetaekon.searchHistory.entity.SearchHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final int MAX_DISPLAY_COUNT = 6;
    private static final long TTL_DAYS = 60; // 데이터 유지 기간(일)


    /**
     * 검색 기록 저장
     */
    @Transactional
    public void saveSearchHistory(Long userId, String searchTerm) {
        // 검색어가 없거나 빈 문자열이면 저장하지 않음
        if (!StringUtils.hasText(searchTerm) || userId == 0L) {
            return;
        }

        // 새로운 검색 기록 생성 및 저장
        SearchHistory newHistory = SearchHistory.of(userId, searchTerm);
        searchHistoryRepository.save(newHistory);
        log.debug("사용자 {} 검색 기록 저장: {}", userId, searchTerm);
    }

    /**
     * 사용자의 검색 기록 조회 (최신 6개)
     */
    @Transactional(readOnly = true)
    public List<SearchHistoryDto> getUserSearchHistories(Long userId) {
        List<SearchHistory> histories = searchHistoryRepository.findByUserId(userId);

        // 최신 순으로 정렬하여 최대 6개 반환
        return histories.stream()
            .sorted(Comparator.comparing(SearchHistory::getCreatedAt).reversed())
            .limit(MAX_DISPLAY_COUNT)
            .map(SearchHistoryDto::from)
            .collect(Collectors.toList());
    }

    /**
     * 개별 검색 기록 삭제 - Redis 직접 접근
     */
    @Transactional
    public void deleteSearchHistory(Long userId, String historyId) {
        if (!StringUtils.hasText(historyId)) {
            return;
        }

        // historyId가 올바른 형식인지 검증 (userId:timestamp 형식)
        if (!historyId.startsWith(userId + ":")) {
            log.warn("잘못된 historyId 형식 또는 권한 없음: {}", historyId);
            return;
        }

        // Redis에서 직접 키 삭제
        boolean deleted = Boolean.TRUE.equals(redisTemplate.delete(historyId));

        if (deleted) {
            log.debug("사용자 {} 검색 기록 삭제 성공: {}", userId, historyId);
        } else {
            log.warn("사용자 {} 검색 기록 삭제 실패: {}", userId, historyId);
        }
    }

    /**
     * 사용자의 모든 검색 기록 삭제
     */
    @Transactional
    public void deleteAllSearchHistories(Long userId) {
        // userId로 시작하는 모든 키 패턴 조회
        String keyPattern = userId + ":*";
        try {
            // 패턴에 일치하는 모든 키 조회
            Set<String> keys = redisTemplate.keys(keyPattern);

            if (keys != null && !keys.isEmpty()) {
                // 모든 키 삭제
                redisTemplate.delete(keys);
                log.debug("사용자 {} 검색 기록 전체 삭제 완료: {}개", userId, keys.size());
            } else {
                log.debug("사용자 {} 삭제할 검색 기록 없음", userId);
            }
        } catch (Exception e) {
            log.error("사용자 {} 검색 기록 전체 삭제 중 오류 발생", userId, e);
        }
    }
}
