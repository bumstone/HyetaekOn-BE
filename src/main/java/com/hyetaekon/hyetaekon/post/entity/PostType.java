package com.hyetaekon.hyetaekon.post.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostType {
    QUESTION("질문"),
    FREE("자유"),
    GREETING("인사");

    @JsonValue
    private final String koreanName;
}
