package com.kokos.onlineshop.service.mapper;

import com.kokos.onlineshop.domain.dto.RegisterRequest;
import com.kokos.onlineshop.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    public User toUser(RegisterRequest request){
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
    }
}
