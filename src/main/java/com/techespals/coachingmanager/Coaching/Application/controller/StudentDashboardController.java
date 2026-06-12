package com.techespals.coachingmanager.Coaching.Application.controller;



import com.techespals.coachingmanager.Coaching.Application.dto.StudentDashboardResponse;
import com.techespals.coachingmanager.Coaching.Application.entity.*;
import com.techespals.coachingmanager.Coaching.Application.repository.AttendanceRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.PaymentRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.StudentRepository;
import com.techespals.coachingmanager.Coaching.Application.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/student/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StudentDashboardController {

    private final CurrentUserService currentUserService;
    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;

    @GetMapping
    public StudentDashboardResponse getDashboard() {

        User currentUser = currentUserService.getCurrentUser();
        Long instituteId = currentUserService.getCurrentInstituteId();

        Student student = studentRepository
                .findByPhoneAndInstituteId(currentUser.getEmail(), instituteId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Attendance> attendanceList =
                attendanceRepository.findByStudentIdAndInstituteId(student.getId(), instituteId);

        int totalAttendance = attendanceList.size();

        int presentDays = (int) attendanceList.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                .count();

        int absentDays = totalAttendance - presentDays;

        double attendancePercentage = totalAttendance == 0
                ? 0
                : (presentDays * 100.0) / totalAttendance;

        List<Payment> recentPayments = paymentRepository
                .findByStudentIdAndInstituteId(student.getId(), instituteId)
                .stream()
                .sorted(Comparator.comparing(Payment::getId).reversed())
                .limit(5)
                .toList();

        return StudentDashboardResponse.builder()
                .student(student)
                .totalAttendance(totalAttendance)
                .presentDays(presentDays)
                .absentDays(absentDays)
                .attendancePercentage(attendancePercentage)
                .recentPayments(recentPayments)
                .build();
    }
}