package com.hyetaekon.hyetaekon.searchHistory.Service;

import com.hyetaekon.hyetaekon.searchHistory.Dto.SearchHistoryDto;
import com.hyetaekon.hyetaekon.searchHistory.Repository.SearchHistoryRepository;
import com.hyetaekon.hyetaekon.searchHistory.entity.SearchHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;

    private static final int MAX_DISPLAY_COUNT = 6;

    /**
     * 검색 기록 저장
     */
    @Transactional
    public void saveSearchHistory(Long userId, String searchTerm) {
        // 검색어가 없거나 빈 문자열이면 저장하지 않음
        if (!StringUtils.hasText(searchTerm) || userId == 0L) {
            return;
        }

        // 검색어 중복 제거를 위한 기존 검색 기록 확인
        List<SearchHistory> existingHistories = searchHistoryRepository.findByUserId(userId);

        // 이미 동일한 검색어가 있으면 삭제
        existingHistories.stream()
            .filter(history -> history.getSearchTerm().equals(searchTerm))
            .forEach(history -> searchHistoryRepository.deleteById(history.getId()));

        // 새로운 검색 기록 저장
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
     * 개별 검색 기록 삭제
     */
    @Transactional
    public void deleteSearchHistory(Long userId, String historyId) {
        if (!StringUtils.hasText(historyId)) {
            return;
        }
        // 사용자의 검색 기록만 삭제하기 위해 사용자 ID도 함께 확인
        searchHistoryRepository.deleteByUserIdAndId(userId, historyId);
        log.debug("사용자 {} 검색 기록 삭제: {}", userId, historyId);
    }

    /**
     * 사용자의 모든 검색 기록 삭제
     */
    @Transactional
    public void deleteAllSearchHistories(Long userId) {
        List<SearchHistory> histories = searchHistoryRepository.findByUserId(userId);
        searchHistoryRepository.deleteAll(histories);
        log.debug("사용자 {} 검색 기록 전체 삭제", userId);
    }
}
