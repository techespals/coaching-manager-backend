package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import com.techespals.coachingmanager.Coaching.Application.entity.User;
import com.techespals.coachingmanager.Coaching.Application.repository.StudentRepository;
import com.techespals.coachingmanager.Coaching.Application.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StudentProfileController {

    private final StudentRepository studentRepository;
    private final CurrentUserService currentUserService;

    @GetMapping("/profile")
    public Student getStudentProfile() {

        User currentUser = currentUserService.getCurrentUser();

        Long instituteId = currentUser.getInstitute().getId();

        return studentRepository
                .findByPhoneAndInstituteId(
                        currentUser.getEmail(),
                        instituteId
                )
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}