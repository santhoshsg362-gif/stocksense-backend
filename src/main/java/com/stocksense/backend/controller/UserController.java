package com.stocksense.backend.controller;

import com.stocksense.backend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            @AuthenticationPrincipal User user) {

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("createdAt", user.getCreatedAt());

        return ResponseEntity.ok(response);
    }
}