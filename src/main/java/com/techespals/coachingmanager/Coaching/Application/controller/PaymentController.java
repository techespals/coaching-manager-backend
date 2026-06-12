package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.dto.PaymentRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.FeeStatus;
import com.techespals.coachingmanager.Coaching.Application.entity.Payment;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import com.techespals.coachingmanager.Coaching.Application.repository.PaymentRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.StudentRepository;
import com.techespals.coachingmanager.Coaching.Application.service.CurrentUserService;
import com.techespals.coachingmanager.Coaching.Application.service.PaymentExcelExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
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
    private final PaymentExcelExportService paymentExcelExportService;

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

        Payment savedPayment = paymentRepository.save(payment);

        String receiptNumber = "RCPT-" + student.getInstitute().getId() + "-" + savedPayment.getId();
        savedPayment.setReceiptNumber(receiptNumber);

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

        return paymentRepository.save(savedPayment);
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        Long instituteId = currentUserService.getCurrentInstituteId();
        return paymentRepository.findByInstituteId(instituteId);
    }

    @GetMapping("/{paymentId}")
    public Payment getPaymentById(@PathVariable Long paymentId) {
        Long instituteId = currentUserService.getCurrentInstituteId();

        return paymentRepository.findByIdAndInstituteId(paymentId, instituteId)
                .orElseThrow(() -> new RuntimeException("Payment not found for this institute"));
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

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportPayments() {

        Long instituteId = currentUserService.getCurrentInstituteId();

        List<Payment> payments = paymentRepository.findByInstituteId(instituteId);

        ByteArrayInputStream excel = paymentExcelExportService.exportPayments(payments);

        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=payments.xlsx"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(new InputStreamResource(excel));
    }
}