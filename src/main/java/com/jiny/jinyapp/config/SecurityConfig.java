package com.jiny.jinyapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 로그인 없이 모든 요청 허용
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // CSRF 비활성화 (필요에 따라 조정)
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
