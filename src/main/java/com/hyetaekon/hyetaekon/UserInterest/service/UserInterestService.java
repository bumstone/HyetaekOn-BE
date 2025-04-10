package com.hyetaekon.hyetaekon.UserInterest.service;

import com.hyetaekon.hyetaekon.UserInterest.dto.UserInterestResponseDto;
import com.hyetaekon.hyetaekon.UserInterest.entity.UserInterest;
import com.hyetaekon.hyetaekon.UserInterest.repository.UserInterestRepository;
import com.hyetaekon.hyetaekon.common.exception.ErrorCode;
import com.hyetaekon.hyetaekon.common.exception.GlobalException;
import com.hyetaekon.hyetaekon.user.entity.User;
import com.hyetaekon.hyetaekon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInterestService {
    private final UserRepository userRepository;
    private final UserInterestRepository userInterestRepository;

    // 나의 관심사 조회
    @Transactional(readOnly = true)
    public UserInterestResponseDto getUserInterestsByUserId(Long userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND_BY_ID));

        List<String> userInterests = user.getInterests().stream()
            .map(UserInterest::getInterest)
            .toList();

        log.debug("회원 관심사 조회 - 유저 ID: {}, 관심사: {}", userId, userInterests);
        return new UserInterestResponseDto(userInterests);
    }

    @Transactional
    public void replaceUserInterests(Long userId, List<String> Interests) {
        // Validate Interest size (optional, assuming max 5)
        if (Interests.size() > 5) {
            throw new GlobalException(
                ErrorCode.INTEREST_LIMIT_EXCEEDED); // Custom error for exceeding limit
        }

        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND_BY_ID));

        // 기존 관심사 제거
        user.getInterests().clear();

        // 새 관심사 추가
        for (String Interest : Interests) {
            UserInterest newInterest = UserInterest.builder()
                .user(user)
                .interest(Interest)
                .build();
            userInterestRepository.save(newInterest);
        }

        log.debug("회원 관심사 갱신 - 유저 ID: {}, 새 관심사 목록: {}", userId, Interests);
    }
}
