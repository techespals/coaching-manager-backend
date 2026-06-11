package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.dto.ChangePasswordRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import com.techespals.coachingmanager.Coaching.Application.entity.User;
import com.techespals.coachingmanager.Coaching.Application.repository.StudentRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.UserRepository;
import com.techespals.coachingmanager.Coaching.Application.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/account")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StudentAccountController {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    @GetMapping("/profile")
    public Student getStudentProfile() {
        User currentUser = currentUserService.getCurrentUser();

        Long instituteId = currentUser.getInstitute().getId();

        return studentRepository.findByPhoneAndInstituteId(
                        currentUser.getEmail(),
                        instituteId
                )
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @PutMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new RuntimeException("Current password is wrong");
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);

        return "Password changed successfully";
    }
}