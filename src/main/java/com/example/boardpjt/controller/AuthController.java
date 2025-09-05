package com.example.boardpjt.controller;

import com.example.boardpjt.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
                           @RequestBody String password) {
        // @Valid -> 유효성 검증 -> 나중에 통합 버전에 추가해놓으시겠대
        userAccountService.register(username, password); // register 라는 서비스로 연결해줌
        return "redirect:/";
        // return "redirect:/auth/login"; // login은 없으니까 403?
    }
}
