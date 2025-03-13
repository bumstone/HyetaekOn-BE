package com.hyetaekon.hyetaekon.user.controller;

import com.hyetaekon.hyetaekon.user.service.AuthService;
import com.hyetaekon.hyetaekon.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {
    public final AuthService authService;


}
