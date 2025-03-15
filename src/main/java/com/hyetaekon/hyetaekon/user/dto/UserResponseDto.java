package com.hyetaekon.hyetaekon.user.dto;

import lombok.Builder;
import lombok.Getter;
import com.hyetaekon.hyetaekon.user.entity.Role;

import java.time.LocalDate;


@Getter
@Builder
public class UserResponseDto {
    private Long id;          // 사용자 ID
    private String name;        // 사용자 이름
    private String email;     // 이메일
    private String nickname;  // 닉네임
    private Role role;      // 권한 (USER/ADMIN 등)

    private LocalDate birthAt; // 생년월일
    private String gender;    // 성별
    private String city;    // 시/도
    private String state;   // 시/군/구

    private String level;   // 회원 등급
    private int point;   // 회원 포인트
}
