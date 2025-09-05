package com.example.boardpjt.controller;

import com.example.boardpjt.service.UserAccountService;
import com.example.boardpjt.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // 이 파일도 화면과 관련된 거니까 Controller. view -> template -> html(thymeleaf)
@RequiredArgsConstructor // 생성자 주입
@RequestMapping("/auth") // 접두사 (prefix) -> /auth/...
public class AuthController {
    private final UserAccountService userAccountService;

    // 회원가입용 페이지로 전달하는 거 필요하고
    @GetMapping("/register") // 접속, GET 방법
    public String registerForm() {
        return "register"; // templates/register.html
    }

    // 해당 처리를 Service로 전달해주는 거 필요함
    @PostMapping("/register") // POST 방법
    // requestbody 같은 경우는 restController에서 쓰임
    public String register(@RequestParam String username,
                           @RequestBody String password,
                           RedirectAttributes redirectAttributes) {
        // @Valid -> 유효성 검증 -> 나중에 통합 버전에 추가해놓으시겠대
        try {
            userAccountService.register(username, password); // register 라는 서비스로 연결해줌
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            // 중복 사용자
            // redirectAttributes.addAttribute("error", e.getMessage()); // Model이 받아서 쓸 수 있게 RequestParam으로 주는 것
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            // 자동으로 model에 넣어줘서 request attribute로 꺼내 쓸 수 있음 // 위에껄로 했더니 주소창에 "이미 존재하는 사용자" 라고 뜸
            // Flash로 해주니까 화면에 글씨 뜸
            return "redirect:/auth/register";
        }
        // return "redirect:/auth/login"; // login은 없으니까 403?
    }

    // 로그인 연결해줄 거야, 메인 페이지의 마이페이지까지 해보자 - 마이페이지는 MainController에
    @GetMapping("/login")
    public String loginForm() {
        return "login"; // login.html
    }

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletResponse response,
                        RedirectAttributes redirectAttributes) {
        try {
            // 인증 시도
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // JWT 발급 -> 쿠키로 저장
            String accessToken = jwtUtil.generateToken(username, "ROLE_" + authentication.getAuthorities().toString(), false);
            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .path("/")
                    // 얜 밀리세컨드가 아닌 그냥 세컨드 기준
                    .maxAge(3600)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            // 마이페이지로 이동
            return "redirect:/my-page";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "로그인 실패");
            return "redirect:/auth/login";
        }
    }
}