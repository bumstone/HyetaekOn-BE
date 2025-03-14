package com.hyetaekon.hyetaekon.common.jwt;

// TODO: Redis
public interface CustomUserPrincipal {
    String getEmail();
    String getNickname();
    String getRole();
    String getName();
}
