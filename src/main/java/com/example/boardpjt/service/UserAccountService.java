package com.example.boardpjt.service;

import com.example.boardpjt.model.entity.UserAccount;
import com.example.boardpjt.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 2개
@Service // Component를 위한 어노테이션
@RequiredArgsConstructor // final 처리 되어있는 필드들에 대한 생성자
// -> 생성자 주입
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserAccount register(String username, String password) {
        if (userAccountRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자"); // 어떤 Exception을 넣어도 상관없지만 걍 Illegal로 넣음
        } // Controller에서 에러 잡아서 처리

        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(username);
        userAccount.setPassword(passwordEncoder.encode(password));
        userAccount.setRole("ROLE_USER");
        return userAccountRepository.save(userAccount);
    }
}