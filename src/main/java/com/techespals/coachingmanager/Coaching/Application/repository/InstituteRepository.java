package com.techespals.coachingmanager.Coaching.Application.repository;



import com.techespals.coachingmanager.Coaching.Application.entity.Institute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstituteRepository extends JpaRepository<Institute, Long> {

    boolean existsByEmail(String email);
}