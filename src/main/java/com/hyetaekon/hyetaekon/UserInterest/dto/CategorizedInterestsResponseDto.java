package com.hyetaekon.hyetaekon.UserInterest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class CategorizedInterestsResponseDto {
    private Map<String, List<String>> categorizedInterests;
}
