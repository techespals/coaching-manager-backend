package com.techespals.coachingmanager.Coaching.Application.service;




import com.techespals.coachingmanager.Coaching.Application.dto.AuthResponse;
import com.techespals.coachingmanager.Coaching.Application.dto.LoginRequest;
import com.techespals.coachingmanager.Coaching.Application.dto.RegisterRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.User;
import com.techespals.coachingmanager.Coaching.Application.repository.UserRepository;
import com.techespals.coachingmanager.Coaching.Application.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        return "User registered successfully";
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token, user.getRole().name());
    }
}