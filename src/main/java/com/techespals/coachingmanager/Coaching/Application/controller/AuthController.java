package com.techespals.coachingmanager.Coaching.Application.controller;




import com.techespals.coachingmanager.Coaching.Application.dto.AuthResponse;
import com.techespals.coachingmanager.Coaching.Application.dto.LoginRequest;
import com.techespals.coachingmanager.Coaching.Application.dto.RegisterRequest;
import com.techespals.coachingmanager.Coaching.Application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}