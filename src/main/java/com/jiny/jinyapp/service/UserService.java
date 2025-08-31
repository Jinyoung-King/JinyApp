package com.jiny.jinyapp.service;

import com.jiny.jinyapp.domain.User;
import com.jiny.jinyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User register(String username, String password, String nickname) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        User user = User.builder()
                .username(username)
                .password(password)  // TODO: BCrypt 적용 권장
                .nickname(nickname)
                .build();

        return userRepository.save(user);
    }

    public User login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 틀렸습니다."));
    }
}
