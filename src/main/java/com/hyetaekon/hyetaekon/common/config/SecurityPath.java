package com.hyetaekon.hyetaekon.common.config;


public class SecurityPath {

  // permitAll
  public static final String[] PUBLIC_ENDPOINTS = {
      "/api/signup",
      "/api/login",
      "/api/token/refresh",
      "/api/users/check-duplicate",
      "/",
      "/api/services",
      "/api/services/popular",
      "/api/services/category/*",
      "/api/services/detail/*",
      "/api/public-data/serviceList/test",
      "/api/mongo/services/search",
      "/api/mongo/services/search/autocomplete"
  };

  // hasRole("USER")
  public static final String[] USER_ENDPOINTS = {
      "/api/users/me",
      "/api/users/me/**",
      "/api/logout",
      "/api/services/*/bookmark",
      "/api/interests",
      "/api/interests/me",
      "/api/posts",
      "/api/posts/*"
  };

  // hasRole("ADMIN")
  public static final String[] ADMIN_ENDPOINTS = {
      "/api/admin/users/**",
      "/api/public-data/serviceDetailList",
      "/api/public-data/supportConditionsList",
      "/api/public-data/serviceList"
  };
}

