package com.example.boardpjt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;

@Configuration // 설정 파일
@EnableWebSecurity // 시큐리티 활성화
public class SecurityConfig {
    // 세 가지 등록해야함
    // 1. Security Filter Chain
    @Bean // 주입해주겠다는 뜻이니까 Bean 달아야 한다.
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http 라고 나와있는 걸 한 번에 해도 되고 나눠서 해도 됨. 결론적으로 build로 한번에 되면 됨

        // 비활성화 설정들이 있고
        http.csrf(AbstractHttpConfigurer::disable) // 내부에 있는 걸 다 비활성화 하겠다로 퉁칠 수 있음
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
                // session policy는 좀 나중에

        // 보안 설정들이 있어
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/auth/register").permitAll()
                .anyRequest().authenticated()
        );

        return http.build();
    }

    // 2. PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        // return new BCryptPasswordEncoder(); // 이렇게 해도 되지만 요즘은 아랫줄처럼 함
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // 비밀번호가 무시되지 않게 하는 것.
    }

    // 3. AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
