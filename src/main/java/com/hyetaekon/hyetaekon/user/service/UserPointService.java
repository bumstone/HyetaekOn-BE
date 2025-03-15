package com.hyetaekon.hyetaekon.user.service;

import com.hyetaekon.hyetaekon.common.exception.ErrorCode;
import com.hyetaekon.hyetaekon.common.exception.GlobalException;
import com.hyetaekon.hyetaekon.user.entity.PointActionType;
import com.hyetaekon.hyetaekon.user.entity.User;
import com.hyetaekon.hyetaekon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserRepository userRepository;
    private final UserLevelService userLevelService;

    // 액션 타입에 따른 포인트 맵핑
    private static final Map<PointActionType, Integer> POINT_VALUES = Map.of(
        PointActionType.POST_CREATION, 10,
        PointActionType.COMMENT_CREATION, 5,
        PointActionType.ANSWER_CREATION, 20
    );

    @Transactional
    public void addPointForAction(Long userId, PointActionType actionType) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND_BY_ID));

        int pointToAdd = POINT_VALUES.getOrDefault(actionType, 0);
        user.addPoint(pointToAdd);

        // 레벨 체크 및 업데이트
        userLevelService.checkAndUpdateLevel(user);

        userRepository.save(user);
    }
}
