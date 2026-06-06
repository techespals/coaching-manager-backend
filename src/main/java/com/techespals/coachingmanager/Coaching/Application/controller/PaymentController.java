package com.techespals.coachingmanager.Coaching.Application.controller;




import com.techespals.coachingmanager.Coaching.Application.dto.PaymentRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.FeeStatus;
import com.techespals.coachingmanager.Coaching.Application.entity.Payment;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import com.techespals.coachingmanager.Coaching.Application.repository.PaymentRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.StudentRepository;
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

    @PostMapping
    public Payment addPayment(@RequestBody PaymentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Payment payment = Payment.builder()
                .student(student)
                .amount(request.getAmount())
                .paymentMode(request.getPaymentMode())
                .paymentDate(LocalDate.now())
                .build();

        student.setPaidFees(student.getPaidFees() + request.getAmount());
        student.setRemainingFees(student.getTotalFees() - student.getPaidFees());

        if (student.getPaidFees() >= student.getTotalFees()) {
            student.setFeeStatus(FeeStatus.PAID);
            student.setRemainingFees(0.0);
        } else if (student.getPaidFees() > 0) {
            student.setFeeStatus(FeeStatus.PARTIAL);
        } else {
            student.setFeeStatus(FeeStatus.UNPAID);
        }

        studentRepository.save(student);

        return paymentRepository.save(payment);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<Payment> getPaymentsByStudent(@PathVariable Long studentId) {
        return paymentRepository.findByStudentId(studentId);
    }
}