package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.dto.AuthResponse;
import com.techespals.coachingmanager.Coaching.Application.dto.LoginRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.Role;
import com.techespals.coachingmanager.Coaching.Application.entity.User;
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

    @PostMapping("/create-super-admins")
    public String createSuperAdmins() {

        createIfNotExists(
                "yanurag1414@gmail.com.com",
                "Anurag Yadav",
                "Lavanu#1"
        );

        createIfNotExists(
                "poorvashi1515@gmail.com",
                "Poorvashi Singh",
                "ASPA@8057"
        );

        return "Super admins created successfully";
    }

    private void createIfNotExists(
            String email,
            String name,
            String password
    ) {

        if (userRepository.existsByEmail(email)) {
            return;
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.SUPER_ADMIN)
                .build();

        userRepository.save(user);
    }
}