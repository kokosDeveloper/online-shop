package com.kokos.onlineshop.service;

import com.kokos.onlineshop.domain.dto.AuthenticationResponse;
import com.kokos.onlineshop.domain.dto.LoginRequest;
import com.kokos.onlineshop.domain.dto.RegisterRequest;
import com.kokos.onlineshop.domain.entity.*;
import com.kokos.onlineshop.exception.AlreadyExistsException;
import com.kokos.onlineshop.repository.TokenRepository;
import com.kokos.onlineshop.repository.UserRepository;
import com.kokos.onlineshop.security.JwtService;
import com.kokos.onlineshop.service.mapper.UserMapper;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    public AuthenticationResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                ));
        var user = (User)authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }
    @Transactional
    public void register(RegisterRequest request) throws MessagingException {
        if (userRepository.existsByEmail(request.email()))
            throw new AlreadyExistsException("User already exists with email: " + request.email());
        User newUser = userMapper.toUser(request);
        createCartForUser(newUser);
        newUser.setRole(Role.USER);
        newUser.setEnabled(false);
        userRepository.save(newUser);
        sendValidationEmail(newUser);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String token = generateAndSaveActivationToken(user);
        emailService.sendAccountActivationToken(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                token
        );
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateToken(7);
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateToken(int length) {
        String characters = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < length; j++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    private void createCartForUser(User newUser) {
        Cart cart = new Cart();
        newUser.setCart(cart);
        cart.setUser(newUser);
    }
    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User doesn't exist with id: " + id));
    }

    public void activateAccount(String stringToken) throws MessagingException {
        Token token = tokenRepository.findByToken(stringToken)
                .orElseThrow(() -> new EntityNotFoundException("Token not found: " + stringToken));
        if (LocalDateTime.now().isAfter(token.getExpiresAt())){
            sendValidationEmail(token.getUser());
            throw new IllegalStateException("Provided token is expired. New token was sent.");
        }
        User user = userRepository.findById(token.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + token.getUser().getId()));
        user.setEnabled(true);
        userRepository.save(user);
        token.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }
}
