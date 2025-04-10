package com.hyetaekon.hyetaekon.user.controller;

import com.hyetaekon.hyetaekon.user.dto.admin.UserAdminResponseDto;
import com.hyetaekon.hyetaekon.user.dto.admin.UserReportResponseDto;
import com.hyetaekon.hyetaekon.user.dto.admin.UserSuspendRequestDto;
import com.hyetaekon.hyetaekon.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService userAdminService;

    /**
     * 회원 목록 조회
     */
    @GetMapping("/users")
    public ResponseEntity<Page<UserAdminResponseDto>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userAdminService.getAllUsers(page, size));
    }

    /**
     * 회원 정지
     */
    @PostMapping("/users/{userId}/suspend")
    public ResponseEntity<Void> suspendUser(
        @PathVariable Long userId,
        @RequestBody UserSuspendRequestDto requestDto) {
        userAdminService.suspendUser(userId, requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 정지 해제
     */
    @PutMapping("/users/{userId}/unsuspend")
    public ResponseEntity<Void> unsuspendUser(@PathVariable Long userId) {
        userAdminService.unsuspendUser(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 정지 회원 목록 조회
     */
    @GetMapping("/users/suspended")
    public ResponseEntity<Page<UserAdminResponseDto>> getSuspendedUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userAdminService.getSuspendedUsers(page, size));
    }

    /**
     * 탈퇴 회원 목록 조회
     */
    @GetMapping("/users/withdrawn")
    public ResponseEntity<Page<UserAdminResponseDto>> getWithdrawnUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userAdminService.getWithdrawnUsers(page, size));
    }

    /**
     * 신고 내역 조회
     */
    @GetMapping("/users/reports")
    public ResponseEntity<Page<UserReportResponseDto>> getUserReports(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userAdminService.getUserReports(page, size));
    }

}
