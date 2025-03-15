package com.hyetaekon.hyetaekon.user.controller;

import com.hyetaekon.hyetaekon.common.jwt.CustomUserDetails;
import com.hyetaekon.hyetaekon.common.jwt.CustomUserPrincipal;
import com.hyetaekon.hyetaekon.user.dto.UserResponseDto;
import com.hyetaekon.hyetaekon.user.dto.UserSignUpRequestDto;
import com.hyetaekon.hyetaekon.user.dto.UserSignUpResponseDto;
import com.hyetaekon.hyetaekon.user.dto.UserUpdateRequestDto;
import com.hyetaekon.hyetaekon.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  // 회원 가입 api
  @PostMapping("/signup")
  public ResponseEntity<UserSignUpResponseDto> registerUser(@RequestBody @Valid UserSignUpRequestDto userSignUpRequestDto) {
    UserSignUpResponseDto userSignUpResponseDto = userService.registerUser(userSignUpRequestDto);
    log.debug("회원가입 성공: email={}", userSignUpRequestDto.getEmail());
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

//    /**
//     * 북마크한 서비스 목록 조회
//     */
//    @GetMapping("/me/bookmarked")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<ApiResponseDto<Page<BookmarkResponseDto>>> getBookmarkedServices(
//        @RequestParam(defaultValue = "0") int page,
//        @RequestParam(defaultValue = "10") int size) {
//        Page<BookmarkResponseDto> bookmarks = userService.getBookmarkedServices(PageRequest.of(page, size));
//        return ResponseEntity.ok(ApiResponseDto.success(bookmarks));
//    }
//
//    /**
//     * 작성한 게시글 목록 조회
//     */
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
//    /**
//     * 작성한 댓글 목록 조회
//     */
//    @GetMapping("/me/comments")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<ApiResponseDto<Page<CommentResponseDto>>> getMyComments(
//        @RequestParam(defaultValue = "0") int page,
//        @RequestParam(defaultValue = "10") int size) {
//        Page<CommentResponseDto> comments = userService.getMyComments(PageRequest.of(page, size));
//        return ResponseEntity.ok(ApiResponseDto.success(comments));
//    }


}
