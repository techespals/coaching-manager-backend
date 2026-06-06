package com.techespals.coachingmanager.Coaching.Application.repository;




import com.techespals.coachingmanager.Coaching.Application.entity.FeeStatus;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByFeeStatus(FeeStatus feeStatus);
    Optional<Student> findByPhone(String phone);
}