package com.hyetaekon.hyetaekon.UserInterest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;


@Getter
@AllArgsConstructor
public class UserInterestResponseDto {
    private List<String> interests;
}
