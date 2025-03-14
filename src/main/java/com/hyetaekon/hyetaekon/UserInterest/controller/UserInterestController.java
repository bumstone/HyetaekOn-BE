package com.hyetaekon.hyetaekon.UserInterest.controller;

import com.hyetaekon.hyetaekon.UserInterest.dto.UserInterestRequestDto;
import com.hyetaekon.hyetaekon.UserInterest.dto.UserInterestResponseDto;
import com.hyetaekon.hyetaekon.UserInterest.entity.UserInterestEnum;
import com.hyetaekon.hyetaekon.UserInterest.service.UserInterestService;
import com.hyetaekon.hyetaekon.common.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class UserInterestController {

    private final UserInterestService userInterestService;

    // 선택할 키워드 목록 조회
    @GetMapping
    public ResponseEntity<UserInterestResponseDto> getAvailableInterests() {
        // Enum에서 displayName 값 추출
        List<String> Interests = Arrays.stream(UserInterestEnum.values())
            .map(UserInterestEnum::getDisplayName)
            .collect(Collectors.toList());
        return ResponseEntity.ok(new UserInterestResponseDto(Interests));
    }


    // 개인 키워드 목록 조회
    @GetMapping("/me")
    public ResponseEntity<UserInterestResponseDto> getMyInterest(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(userInterestService.getUserInterestsByUserId(userId));
    }

    // 선택한 키워드 목록 저장
    @PostMapping("/me")
    public ResponseEntity<Void> replaceInterests(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody UserInterestRequestDto UserInterestRequest
    ) {
        Long userId = userDetails.getId();
        List<String> Interests = UserInterestRequest.getInterests();
        userInterestService.replaceUserInterests(userId, Interests);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
