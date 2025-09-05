package com.example.boardpjt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;

@Configuration // 설정 파일
@EnableWebSecurity // 시큐리티 활성화
public class SecurityConfig {
    // 세 가지 등록해야함
    // 1. Security Filter Chain
    // 2. PasswordEncoder
    // 3. AuthenticationManager


}
