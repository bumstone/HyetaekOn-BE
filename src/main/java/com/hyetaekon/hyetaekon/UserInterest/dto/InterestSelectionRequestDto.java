package com.hyetaekon.hyetaekon.UserInterest.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class InterestSelectionRequestDto {
    private Map<String, List<String>> categorizedInterests;

    // 기존 필드 유지
    private List<String> selectedInterests;

    // categorizedInterests에서 모든 관심사를 추출하는 메소드
    public List<String> getAllInterests() {
        if (selectedInterests != null) {
            return selectedInterests;
        }

        if (categorizedInterests == null) {
            return new ArrayList<>();
        }

        List<String> allInterests = new ArrayList<>();
        categorizedInterests.values().forEach(allInterests::addAll);
        return allInterests;
    }
}