package com.hyetaekon.hyetaekon.common.public_data.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PublicDataPath {
    SERVICE_LIST("/serviceList"),
    SERVICE_DETAIL_LIST("/serviceDetailList"),
    SERVICE_CONDITIONS_LIST("/supportConditionsList");

    private final String path;
}
