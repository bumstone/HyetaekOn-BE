package com.hyetaekon.hyetaekon.common.config;


public class SecurityPath {

  // permitAll
  public static final String[] PUBLIC_ENDPOINTS = {
      "/api/signup",
      "/api/login",
      "/api/token/refresh",
      "/api/users/check-duplicate",
      "/",
      "/api/services/category/*",
      "/api/services/detail/*"
  };


  // hasRole("USER")
  public static final String[] USER_ENDPOINTS = {
      "/api/users/me/bookmarked",
      "/api/users/me/*",
      "/api/users/me",
      "/api/logout",
      "/api/services/popular"
  };

  // hasRole("ADMIN")
  public static final String[] ADMIN_ENDPOINTS = {
      "/api/admin/**",
      "/api/public-data/serviceDetailList",
      "/api/public-data/supportConditionsList",
      "/api/public-data/serviceList/**"
  };
}

