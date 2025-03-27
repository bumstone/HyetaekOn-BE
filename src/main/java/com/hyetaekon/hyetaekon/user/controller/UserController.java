package com.hyetaekon.hyetaekon.user.controller;

import com.hyetaekon.hyetaekon.common.jwt.CustomUserDetails;
import com.hyetaekon.hyetaekon.publicservice.dto.PublicServiceListResponseDto;
import com.hyetaekon.hyetaekon.publicservice.service.PublicServiceHandler;
import com.hyetaekon.hyetaekon.user.dto.UserResponseDto;
import com.hyetaekon.hyetaekon.user.dto.UserSignUpRequestDto;
import com.hyetaekon.hyetaekon.user.dto.UserSignUpResponseDto;
import com.hyetaekon.hyetaekon.user.dto.UserUpdateRequestDto;
import com.hyetaekon.hyetaekon.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final PublicServiceHandler publicServiceHandler;

  // 회원 가입 api
  @PostMapping("/signup")
  public ResponseEntity<UserSignUpResponseDto> registerUser(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto) {
    UserSignUpResponseDto userSignUpResponseDto = userService.registerUser(userSignUpRequestDto);
    log.debug("회원가입 성공: realId={}", userSignUpRequestDto.getRealId());
    return ResponseEntity.status(HttpStatus.CREATED).body(userSignUpResponseDto);
  }

  // 회원 정보 조회 api
  @GetMapping("/users/me")
  public ResponseEntity<UserResponseDto> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
    Long userId = userDetails.getId();
    UserResponseDto userInfo = userService.getMyInfo(userId);

    return ResponseEntity.ok(userInfo);
  }

  // 회원 정보 수정 api
  @PutMapping("/users/me")
  public ResponseEntity<UserResponseDto> updateMyInfo(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto
  ) {

    Long userId = userDetails.getId();

    return ResponseEntity.ok(userService.updateUser(userId, userUpdateRequestDto));
  }

  // 회원 탈퇴
  @DeleteMapping("/users/me")
  public ResponseEntity<Void> deleteUser(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      @RequestBody String deleteReason,
      @CookieValue(name = "refreshToken", required = false) String refreshToken,
      @RequestHeader("Authorization") String authHeader
  ) {
    String accessToken = authHeader.replace("Bearer ", "");
    userService.deleteUser(customUserDetails.getId(), deleteReason, accessToken, refreshToken);

    return ResponseEntity.noContent().build();
  }

  // 중복 확인 api
  @GetMapping("/users/check-duplicate")
  public boolean checkDuplicate(
      @RequestParam(value = "type") String type,
      @RequestParam(value = "value") String value) {

    return userService.checkDuplicate(type, value);
  }

  // 북마크한 서비스 목록 조회
  @GetMapping("/users/me/bookmarked")
  public ResponseEntity<Page<PublicServiceListResponseDto>> getBookmarkedServices(
      @RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
      @RequestParam(name = "size", defaultValue = "9") @Positive @Max(30) int size,
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(publicServiceHandler.getBookmarkedServices(
        userDetails.getId(), PageRequest.of(page, size))
    );
  }

    /**
     * 작성한 게시글 목록 조회
     */
//    @GetMapping("/me/posts")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<ApiResponseDto<Page<PostResponseDto>>> getMyPosts(
//        @RequestParam(required = false) String postType,
//        @RequestParam(defaultValue = "0") int page,
//        @RequestParam(defaultValue = "10") int size) {
//        Page<PostResponseDto> posts = userService.getMyPosts(postType, PageRequest.of(page, size));
//        return ResponseEntity.ok(ApiResponseDto.success(posts));
//    }
//
    /**
     * 작성한 댓글 목록 조회
     */
//    @GetMapping("/me/comments")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<ApiResponseDto<Page<CommentResponseDto>>> getMyComments(
//        @RequestParam(defaultValue = "0") int page,
//        @RequestParam(defaultValue = "10") int size) {
//        Page<CommentResponseDto> comments = userService.getMyComments(PageRequest.of(page, size));
//        return ResponseEntity.ok(ApiResponseDto.success(comments));
//    }


}
