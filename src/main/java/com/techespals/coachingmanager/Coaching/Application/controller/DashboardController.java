package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.dto.DashboardResponse;
import com.techespals.coachingmanager.Coaching.Application.entity.Attendance;
import com.techespals.coachingmanager.Coaching.Application.entity.AttendanceStatus;
import com.techespals.coachingmanager.Coaching.Application.entity.Payment;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import com.techespals.coachingmanager.Coaching.Application.repository.AttendanceRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.BatchRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.CourseRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.PaymentRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.StudentRepository;
import com.techespals.coachingmanager.Coaching.Application.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/owner/dashboard")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DashboardController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;
    private final PaymentRepository paymentRepository;
    private final AttendanceRepository attendanceRepository;
    private final CurrentUserService currentUserService;

    @GetMapping
    public DashboardResponse getDashboard() {

        Long instituteId = currentUserService.getCurrentInstituteId();

        List<Student> students = studentRepository.findByInstituteId(instituteId);
        List<Payment> payments = paymentRepository.findByInstituteId(instituteId);
        List<Payment> todayPayments =
                paymentRepository.findByPaymentDateAndInstituteId(LocalDate.now(), instituteId);

        double pendingFees = students.stream()
                .mapToDouble(s -> s.getRemainingFees() == null ? 0 : s.getRemainingFees())
                .sum();

        int pendingStudents = (int) students.stream()
                .filter(s -> s.getRemainingFees() != null && s.getRemainingFees() > 0)
                .count();

        double totalCollection = payments.stream()
                .mapToDouble(p -> p.getAmount() == null ? 0 : p.getAmount())
                .sum();

        double todayCollection = todayPayments.stream()
                .mapToDouble(p -> p.getAmount() == null ? 0 : p.getAmount())
                .sum();

        List<Attendance> todayAttendance =
                attendanceRepository.findByDateAndInstituteId(LocalDate.now(), instituteId);

        int todayPresent = (int) todayAttendance.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                .count();

        int todayAbsent = (int) todayAttendance.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ABSENT)
                .count();

        int totalTodayAttendance = todayPresent + todayAbsent;

        double todayAttendancePercentage = totalTodayAttendance == 0
                ? 0
                : (todayPresent * 100.0) / totalTodayAttendance;

        Map<String, Double> monthlyCollection = getMonthlyCollection(payments);

        List<Student> latestStudents = students.stream()
                .sorted(Comparator.comparing(Student::getId).reversed())
                .limit(5)
                .toList();

        List<Payment> latestPayments = payments.stream()
                .sorted(Comparator.comparing(Payment::getId).reversed())
                .limit(5)
                .toList();

        return DashboardResponse.builder()
                .totalStudents(students.size())
                .totalCourses(courseRepository.findByInstituteId(instituteId).size())
                .totalBatches(batchRepository.findByInstituteId(instituteId).size())
                .totalCollection(totalCollection)
                .todayCollection(todayCollection)
                .pendingFees(pendingFees)
                .pendingStudents(pendingStudents)
                .todayPresent(todayPresent)
                .todayAbsent(todayAbsent)
                .todayAttendancePercentage(todayAttendancePercentage)
                .monthlyCollection(monthlyCollection)
                .latestStudents(latestStudents)
                .latestPayments(latestPayments)
                .build();
    }

    private Map<String, Double> getMonthlyCollection(List<Payment> payments) {
        Map<String, Double> monthlyMap = new LinkedHashMap<>();

        for (Month month : Month.values()) {
            monthlyMap.put(month.name().substring(0, 3), 0.0);
        }

        for (Payment payment : payments) {
            if (payment.getPaymentDate() == null) continue;

            String monthName = payment.getPaymentDate()
                    .getMonth()
                    .name()
                    .substring(0, 3);

            double currentAmount = monthlyMap.getOrDefault(monthName, 0.0);
            double paymentAmount = payment.getAmount() == null ? 0 : payment.getAmount();

            monthlyMap.put(monthName, currentAmount + paymentAmount);
        }

        return monthlyMap;
    }
}