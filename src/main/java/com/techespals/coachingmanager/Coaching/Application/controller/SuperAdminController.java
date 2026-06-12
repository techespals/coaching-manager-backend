package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.dto.CreateInstituteRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.Institute;
import com.techespals.coachingmanager.Coaching.Application.entity.Role;
import com.techespals.coachingmanager.Coaching.Application.entity.User;
import com.techespals.coachingmanager.Coaching.Application.repository.InstituteRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SuperAdminController {

    private final InstituteRepository instituteRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();

        dashboard.put("totalInstitutes", instituteRepository.count());
        dashboard.put("activeInstitutes", instituteRepository.countByActive(true));
        dashboard.put("inactiveInstitutes", instituteRepository.countByActive(false));
        dashboard.put("totalInstituteAdmins", userRepository.countByRole(Role.INSTITUTE_ADMIN));
        dashboard.put("totalStudents", userRepository.countByRole(Role.STUDENT));

        return dashboard;
    }

    @GetMapping("/institutes")
    public List<Institute> getAllInstitutes() {
        return instituteRepository.findAll();
    }

    @PostMapping("/institutes")
    public String createInstitute(@RequestBody CreateInstituteRequest request) {

        if (instituteRepository.existsByEmail(request.getEmail())) {
            return "Institute email already exists";
        }

        if (userRepository.existsByEmail(request.getAdminEmail())) {
            return "Admin email already exists";
        }

        Institute institute = Institute.builder()
                .instituteName(request.getInstituteName())
                .ownerName(request.getOwnerName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .active(true)
                .build();

        Institute savedInstitute = instituteRepository.save(institute);

        User admin = User.builder()
                .name(request.getAdminName())
                .email(request.getAdminEmail())
                .password(passwordEncoder.encode(request.getAdminPassword()))
                .role(Role.INSTITUTE_ADMIN)
                .institute(savedInstitute)
                .build();

        userRepository.save(admin);

        return "Institute and admin created successfully";
    }

    @PutMapping("/institutes/{id}/toggle-status")
    public Institute toggleInstituteStatus(@PathVariable Long id) {
        Institute institute = instituteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institute not found"));

        Boolean currentStatus = institute.getActive() != null && institute.getActive();
        institute.setActive(!currentStatus);

        return instituteRepository.save(institute);
    }
}