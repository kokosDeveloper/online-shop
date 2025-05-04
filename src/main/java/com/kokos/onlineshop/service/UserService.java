package com.kokos.onlineshop.service;

import com.kokos.onlineshop.domain.dto.AuthenticationResponse;
import com.kokos.onlineshop.domain.dto.LoginRequest;
import com.kokos.onlineshop.domain.dto.RegisterRequest;
import com.kokos.onlineshop.domain.entity.Cart;
import com.kokos.onlineshop.domain.entity.User;
import com.kokos.onlineshop.exception.AlreadyExistsException;
import com.kokos.onlineshop.repository.UserRepository;
import com.kokos.onlineshop.security.JwtService;
import com.kokos.onlineshop.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
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
    public void register(RegisterRequest request){
        if (userRepository.existsByEmail(request.email()))
            throw new AlreadyExistsException("User already exists with email: " + request.email());
        User newUser = userMapper.toUser(request);
        createCartForUser(newUser);
        newUser.setRoles(Set.of("ROLE_USER"));
        userRepository.save(newUser);
    }

    private void createCartForUser(User newUser) {
        Cart cart = new Cart();
        newUser.setCart(cart);
        cart.setUser(newUser);
    }
}
