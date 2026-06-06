package com.techespals.coachingmanager.Coaching.Application.repository;




import com.techespals.coachingmanager.Coaching.Application.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStudentId(Long studentId);
}
