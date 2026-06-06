package com.techespals.coachingmanager.Coaching.Application.controller;



import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import com.techespals.coachingmanager.Coaching.Application.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StudentProfileController {

    private final StudentRepository studentRepository;

    @GetMapping("/profile/{phone}")
    public Student getStudentProfile(@PathVariable String phone) {
        return studentRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}