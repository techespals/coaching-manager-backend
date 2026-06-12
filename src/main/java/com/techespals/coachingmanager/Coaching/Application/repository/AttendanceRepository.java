package com.techespals.coachingmanager.Coaching.Application.repository;

import com.techespals.coachingmanager.Coaching.Application.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudentIdAndInstituteId(Long studentId, Long instituteId);

    List<Attendance> findByBatchIdAndDateAndInstituteId(
            Long batchId,
            LocalDate date,
            Long instituteId
    );

    List<Attendance> findByBatchIdAndInstituteId(Long batchId, Long instituteId);

    Optional<Attendance> findByStudentIdAndDateAndInstituteId(
            Long studentId,
            LocalDate date,
            Long instituteId
    );

    List<Attendance> findByDateAndInstituteId(LocalDate date, Long instituteId);
}