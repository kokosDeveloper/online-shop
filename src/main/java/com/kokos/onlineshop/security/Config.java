package com.kokos.onlineshop.security;

import com.kokos.onlineshop.domain.entity.Permission;
import com.kokos.onlineshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class Config {
    private final UserRepository userRepository;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            return userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        };
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.GET,
                        "/categories/**",
                        "/products/**").permitAll()
                //products
                .requestMatchers(HttpMethod.POST, "/products/**").hasAuthority(Permission.ADMIN_ADD_PRODUCT.getPermission())
                .requestMatchers(HttpMethod.DELETE, "/products/**").hasAuthority(Permission.ADMIN_DELETE_PRODUCT.getPermission())
                .requestMatchers(HttpMethod.PUT, "/products/**").hasAuthority(Permission.ADMIN_UPDATE_PRODUCT.getPermission())

                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                            response.getWriter().write("Unauthorized.");
                                        })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.setStatus(HttpStatus.FORBIDDEN.value());
                                    response.getWriter().write("Unauthorized");
                                }));


        return http.build();
    }
}
