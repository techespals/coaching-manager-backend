package com.techespals.coachingmanager.Coaching.Application.repository;

import com.techespals.coachingmanager.Coaching.Application.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByInstituteId(Long instituteId);

    List<Payment> findByStudentIdAndInstituteId(Long studentId, Long instituteId);

    List<Payment> findByPaymentDateAndInstituteId(LocalDate paymentDate, Long instituteId);
}