package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.dto.StudentRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import com.techespals.coachingmanager.Coaching.Application.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/students")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public Student addStudent(@RequestBody StudentRequest request) {
        return studentService.addStudent(request);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public Student updateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequest request
    ) {
        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/unpaid")
    public List<Student> unpaidStudents() {
        return studentService.getUnpaidStudents();
    }

    @GetMapping("/partial")
    public List<Student> partialFeeStudents() {
        return studentService.getPartialFeeStudents();
    }

    @GetMapping("/sort/name")
    public List<Student> sortByName() {
        return studentService.sortByName();
    }

    @GetMapping("/sort/remaining-fees")
    public List<Student> sortByRemainingFees() {
        return studentService.sortByRemainingFees();
    }
}