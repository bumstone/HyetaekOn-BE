package com.hyetaekon.hyetaekon.user.mapper;

import com.hyetaekon.hyetaekon.user.dto.UserResponseDto;
import com.hyetaekon.hyetaekon.user.dto.UserSignUpResponseDto;
import com.hyetaekon.hyetaekon.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    /*// 회원가입 요청 DTO -> User Entity 변환
    @Mapping(target = "role", constant = "ROLE_USER") // 기본 Role 설정
    User toEntity(UserSignUpRequestDto dto);*/

    // User Entity -> 회원가입 응답 DTO 변환
    UserSignUpResponseDto toSignUpResponseDto(User user);

    // User Entity -> 회원 정보 조회 DTO 변환
    UserResponseDto toResponseDto(User user);
}
