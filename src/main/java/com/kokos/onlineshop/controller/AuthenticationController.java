package com.kokos.onlineshop.controller;

import com.kokos.onlineshop.domain.dto.LoginRequest;
import com.kokos.onlineshop.domain.dto.AuthenticationResponse;
import com.kokos.onlineshop.domain.dto.RegisterRequest;
import com.kokos.onlineshop.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request
            ) throws MessagingException {
        userService.register(request);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/activate-account")
    public ResponseEntity<Void> activateAccount(
            @RequestParam String token
    ) throws MessagingException {
        userService.activateAccount(token);
        return ResponseEntity.ok().build();
    }

}
