package com.techespals.coachingmanager.Coaching.Application.repository;

import com.techespals.coachingmanager.Coaching.Application.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BatchRepository extends JpaRepository<Batch, Long> {

    List<Batch> findByInstituteId(Long instituteId);

    Optional<Batch> findByIdAndInstituteId(Long id, Long instituteId);
}