package com.example.boardpjt.filter;

import com.example.boardpjt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
// 요청이 들어갔을 때 하는 거 OncePerRequestFilter -> 이거 정확히 뭔지 보기
public class JwtFilter extends OncePerRequestFilter {
    // SecurityConfig

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    // 스프링으로 관리하진 않을텐데 -> SecurityConfig에서 주입할 예정

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        if (request.getCookies() != null) { // 요청에 쿠키가 있는데
            for (Cookie c : request.getCookies()) {
                // access_token
                if (c.getName().equals("access_token")) {
                    token = c.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            filterChain.doFilter(request, response); // 다음 필터로 넘겨
            return; // 이거 return 안 하면 두 개 그려짐
        }

        try {
            String username = jwtUtil.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            Authentication authentication =
                    // UPAT
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            // token의 Username으로 찾아낸 인증 정보를 SecurityContextHolder에 주입
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // 나중에 꼬이지 않게 미리
        filterChain.doFilter(request, response);
    }
}