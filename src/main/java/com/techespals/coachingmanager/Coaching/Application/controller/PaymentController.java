package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.dto.PaymentRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.FeeStatus;
import com.techespals.coachingmanager.Coaching.Application.entity.Payment;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import com.techespals.coachingmanager.Coaching.Application.repository.PaymentRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.StudentRepository;
import com.techespals.coachingmanager.Coaching.Application.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/owner/payments")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;
    private final CurrentUserService currentUserService;

    @PostMapping
    public Payment addPayment(@RequestBody PaymentRequest request) {
        Long instituteId = currentUserService.getCurrentInstituteId();

        Student student = studentRepository
                .findByIdAndInstituteId(request.getStudentId(), instituteId)
                .orElseThrow(() -> new RuntimeException("Student not found for this institute"));

        if (request.getAmount() <= 0) {
            throw new RuntimeException("Payment amount must be greater than zero");
        }

        double currentPaid = student.getPaidFees() == null ? 0 : student.getPaidFees();
        double totalFees = student.getTotalFees() == null ? 0 : student.getTotalFees();

        double newPaidFees = currentPaid + request.getAmount();

        if (newPaidFees > totalFees) {
            throw new RuntimeException("Payment amount is greater than remaining fees");
        }

        Payment payment = Payment.builder()
                .student(student)
                .institute(student.getInstitute())
                .amount(request.getAmount())
                .paymentMode(request.getPaymentMode())
                .paymentDate(LocalDate.now())
                .build();

        student.setPaidFees(newPaidFees);
        student.setRemainingFees(totalFees - newPaidFees);

        if (newPaidFees >= totalFees) {
            student.setFeeStatus(FeeStatus.PAID);
            student.setRemainingFees(0.0);
        } else if (newPaidFees > 0) {
            student.setFeeStatus(FeeStatus.PARTIAL);
        } else {
            student.setFeeStatus(FeeStatus.UNPAID);
        }

        studentRepository.save(student);

        return paymentRepository.save(payment);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        Long instituteId = currentUserService.getCurrentInstituteId();
        return paymentRepository.findByInstituteId(instituteId);
    }

    @GetMapping("/today")
    public List<Payment> getTodayPayments() {
        Long instituteId = currentUserService.getCurrentInstituteId();
        return paymentRepository.findByPaymentDateAndInstituteId(LocalDate.now(), instituteId);
    }

    @GetMapping("/student/{studentId}")
    public List<Payment> getPaymentsByStudent(@PathVariable Long studentId) {
        Long instituteId = currentUserService.getCurrentInstituteId();
        return paymentRepository.findByStudentIdAndInstituteId(studentId, instituteId);
    }
}