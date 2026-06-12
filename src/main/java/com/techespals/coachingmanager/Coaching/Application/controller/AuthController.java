package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.dto.AuthResponse;
import com.techespals.coachingmanager.Coaching.Application.dto.LoginRequest;
import com.techespals.coachingmanager.Coaching.Application.repository.UserRepository;
import com.techespals.coachingmanager.Coaching.Application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}