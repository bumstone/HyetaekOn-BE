package com.hyetaekon.hyetaekon.common.jwt;

public interface CustomUserPrincipal {
    String getEmail();
    String getNickname();
    String getRole();
    String getName();
}
