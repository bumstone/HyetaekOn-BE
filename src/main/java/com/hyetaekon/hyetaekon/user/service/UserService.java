package com.hyetaekon.hyetaekon.user.service;

import com.hyetaekon.hyetaekon.common.exception.ErrorCode;
import com.hyetaekon.hyetaekon.common.exception.GlobalException;
import com.hyetaekon.hyetaekon.common.jwt.BlacklistService;
import com.hyetaekon.hyetaekon.common.jwt.RefreshTokenService;
import com.hyetaekon.hyetaekon.user.dto.UserResponseDto;
import com.hyetaekon.hyetaekon.user.dto.UserSignUpRequestDto;
import com.hyetaekon.hyetaekon.user.dto.UserSignUpResponseDto;
import com.hyetaekon.hyetaekon.user.dto.UserUpdateRequestDto;
import com.hyetaekon.hyetaekon.user.entity.Role;
import com.hyetaekon.hyetaekon.user.entity.User;
import com.hyetaekon.hyetaekon.user.mapper.UserMapper;
import com.hyetaekon.hyetaekon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    private final BlacklistService blacklistService;


    // 회원 가입
    // TODO: Occupation, BusinessType 재확인
    @Transactional
    public UserSignUpResponseDto registerUser(UserSignUpRequestDto userSignUpRequestDto) {
        // 이메일 또는 닉네임 중복 검사
        Optional<User> existingUser = userRepository.findByRealIdOrNicknameAndDeletedAtIsNull(
            userSignUpRequestDto.getRealId(),
            userSignUpRequestDto.getNickname()
        );

        if (existingUser.isPresent()) {
            User user = existingUser.get(); // NPE 방지
            if (user.getRealId().equals(userSignUpRequestDto.getRealId())) {
                throw new GlobalException(ErrorCode.DUPLICATED_REAL_ID);
            }
            if (user.getNickname().equals(userSignUpRequestDto.getNickname())) {
                throw new GlobalException(ErrorCode.DUPLICATED_NICKNAME);
            }
        }

        String encodedPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());

        // 추가 필드를 포함한 User 객체 생성
        User newUser = User.builder()
            .realId(userSignUpRequestDto.getRealId())
            .nickname(userSignUpRequestDto.getNickname())
            .password(encodedPassword)
            .name(userSignUpRequestDto.getName())
            .birthAt(userSignUpRequestDto.getBirthAt())
            .gender(userSignUpRequestDto.getGender())
            .city(userSignUpRequestDto.getCity())
            .state(userSignUpRequestDto.getState())
            .role(Role.ROLE_USER)
            .point(0) // 초기 포인트 설정
            .createdAt(LocalDateTime.now()) // 생성 시간 설정
            .build();

        User savedUser = userRepository.save(newUser);
        log.debug("회원 가입 - 이메일: {}", savedUser.getRealId());

        return userMapper.toSignUpResponseDto(savedUser);
    }

    // 회원 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDto getMyInfo(Long userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND_BY_ID));

        return userMapper.toResponseDto(user);
    }

    // 이메일로 회원 검색
    @Transactional(readOnly = true)
    public User findUserByRealId(String realId) {
        return userRepository.findByRealIdAndDeletedAtIsNull(realId)
            .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND_BY_REAL_ID));

    }


    // 회원 정보 수정(닉네임, 비밀번호, 이름, 성별, 생년월일, 지역)
    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto userUpdateRequestDto) {
        // 사용자 정보 조회
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND_BY_ID));

        // 닉네임 변경
        String newNickname = userUpdateRequestDto.getNickname();
        if (newNickname != null && !newNickname.isBlank()) { // 닉네임이 null 또는 공백이 아닐 때만 처리
            if (userRepository.existsByNickname(newNickname)) {
                throw new GlobalException(ErrorCode.DUPLICATED_NICKNAME);
            }
            user.updateNickname(newNickname); // 닉네임 변경
        }

        // 이름 변경
        String newName = userUpdateRequestDto.getName();
        if (newName != null && !newName.isBlank()) {
            user.updateName(newName);
        }

        // 생년월일 변경
        if (userUpdateRequestDto.getBirthAt() != null) {
            user.updateBirthAt(userUpdateRequestDto.getBirthAt());
        }

        // 성별 변경
        String newGender = userUpdateRequestDto.getGender();
        if (newGender != null && !newGender.isBlank()) {
            user.updateGender(newGender);
        }

        // 지역 변경
        String newCity = userUpdateRequestDto.getCity();
        if (newCity != null && !newCity.isBlank()) {
            user.updateCity(newCity);
        }

        // 지역 변경
        String newState = userUpdateRequestDto.getState();
        if (newState != null && !newState.isBlank()) {
            user.updateState(newState);
        }

        // 비밀번호 변경
        String currentPassword = userUpdateRequestDto.getCurrentPassword();
        String newPassword = userUpdateRequestDto.getNewPassword();

        // 새 비밀번호가 있을 때만 비밀번호 변경 로직 실행
        if (newPassword != null && !newPassword.isBlank()) {
            // 현재 비밀번호가 입력되지 않았으면 에러 발생
            if (currentPassword == null || currentPassword.isBlank()) {
                throw new GlobalException(ErrorCode.CURRENT_PASSWORD_REQUIRED);
            }

            // 현재 비밀번호가 맞는지 확인
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new GlobalException(ErrorCode.PASSWORD_MISMATCH);
            }

            // 새 비밀번호가 기존 비밀번호와 같으면 에러 발생
            if (currentPassword.equals(newPassword)) {
                throw new GlobalException(ErrorCode.PASSWORD_SAME_AS_OLD);
            }

            user.updatePassword(passwordEncoder.encode(newPassword)); // 비밀번호 변경
        }

        // 변경된 사용자 정보 저장
        User updatedUser = userRepository.save(user);
        log.debug("회원 정보 업데이트 - 이메일: {}", updatedUser.getRealId());
        return userMapper.toResponseDto(updatedUser);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId, String deleteReason, String accessToken, String refreshToken) {
        // 사용자 정보 조회
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND_BY_ID));

        // redis에서 리프레시 토큰 삭제
        refreshTokenService.deleteRefreshToken(refreshToken);
        // access token 블랙리스트에 등록
        blacklistService.addToBlacklist(accessToken);

        // 사용자 탈퇴 처리 (소프트 삭제)
        user.deleteUser(deleteReason);
        log.debug("회원 탈퇴 - 이메일: {} , 탈퇴 사유: {}", userId, deleteReason);

    }

    // 중복 확인(회원 가입시 아이디, 닉네임 부분)
    public boolean checkDuplicate(String type, String value) {
        return switch (type.toLowerCase()) {
            case "realid" -> userRepository.existsByRealIdAndDeletedAtIsNull(value);
            case "nickname" -> userRepository.existsByNickname(value);
            default -> throw new IllegalArgumentException("잘못된 타입 입력값입니다.");
        };
    }

}
