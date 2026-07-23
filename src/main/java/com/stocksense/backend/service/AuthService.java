package com.stocksense.backend.service;

import com.stocksense.backend.config.JwtService;
import com.stocksense.backend.dto.AuthResponse;
import com.stocksense.backend.dto.LoginRequest;
import com.stocksense.backend.dto.RegisterRequest;
import com.stocksense.backend.model.User;
import com.stocksense.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .message("Registration successful")
                .build();
    }

    public AuthResponse login(LoginRequest request) {

        // Step 1 - Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Step 2 - Check if password matches the stored hash
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Step 3 - Generate a fresh JWT token
        String token = jwtService.generateToken(user.getEmail());

        // Step 4 - Return token and user info
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .message("Login successful")
                .build();
    }
}