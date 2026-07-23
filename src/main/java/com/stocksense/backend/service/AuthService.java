package com.stocksense.backend.service;

import com.stocksense.backend.config.JwtService;
import com.stocksense.backend.dto.AuthResponse;
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

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Build the User object
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // Save to database
        userRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(user.getEmail());

        // Return response
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .message("Registration successful")
                .build();
    }
}