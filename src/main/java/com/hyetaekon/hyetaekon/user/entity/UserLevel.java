package com.hyetaekon.hyetaekon.user.entity;

import lombok.Getter;

@Getter
public enum UserLevel {
    BEGINNER(0, 99),
    INTERMEDIATE(100, 199),
    ADVANCED(200, 299),
    EXPERT(300, Integer.MAX_VALUE);

    private final int minPoint;
    private final int maxPoint;

    UserLevel(int minPoint, int maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
    }

    public static UserLevel fromPoint(int point) {
        for (UserLevel level : values()) {
            if (point >= level.minPoint && point <= level.maxPoint) {
                return level;
            }
        }
        return BEGINNER; // 기본값
    }

}
