package com.hyetaekon.hyetaekon.user.service;

import com.hyetaekon.hyetaekon.common.exception.ErrorCode;
import com.hyetaekon.hyetaekon.common.exception.GlobalException;
import com.hyetaekon.hyetaekon.post.repository.PostRepository;
import com.hyetaekon.hyetaekon.user.entity.PointActionType;
import com.hyetaekon.hyetaekon.user.entity.User;
import com.hyetaekon.hyetaekon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserRepository userRepository;
    private final UserLevelService userLevelService;
    private final PostRepository postRepository; // 게시글 저장소 추가 필요

    @Transactional
    public void addPointForAction(Long userId, PointActionType actionType) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND_BY_ID));

        int pointToAdd = actionType.getPoints();

        // 게시글 작성인 경우 첫 게시글 여부 확인
        if (actionType == PointActionType.POST_CREATION) {
            boolean isFirstPost = !postRepository.existsByUserIdAndDeletedAtIsNull(userId);
            if (isFirstPost) {
                // 첫 게시글인 경우 FIRST_POST_CREATION 포인트 적용
                pointToAdd = PointActionType.FIRST_POST_CREATION.getPoints();
                log.info("사용자 {}의 첫 게시글 작성으로 {}점 획득", userId, pointToAdd);
            }
        }

        user.addPoint(pointToAdd);

        // 레벨 체크 및 업데이트
        userLevelService.checkAndUpdateLevel(user);

        userRepository.save(user);
    }
}
