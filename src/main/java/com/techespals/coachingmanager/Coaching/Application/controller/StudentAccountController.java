package com.techespals.coachingmanager.Coaching.Application.controller;


import com.techespals.coachingmanager.Coaching.Application.dto.ChangePasswordRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.User;
import com.techespals.coachingmanager.Coaching.Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/account")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StudentAccountController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is wrong");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }
}