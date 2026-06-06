package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.dto.AttendanceRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.Attendance;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import com.techespals.coachingmanager.Coaching.Application.repository.AttendanceRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/owner/attendance")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    @PostMapping
    public Attendance markAttendance(@RequestBody AttendanceRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Attendance attendance = Attendance.builder()
                .student(student)
                .status(request.getStatus())
                .date(LocalDate.now())
                .build();

        return attendanceRepository.save(attendance);
    }

    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<Attendance> getStudentAttendance(@PathVariable Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
}