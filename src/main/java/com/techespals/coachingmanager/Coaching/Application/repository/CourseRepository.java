package com.techespals.coachingmanager.Coaching.Application.repository;

import com.techespals.coachingmanager.Coaching.Application.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByInstituteId(Long instituteId);

    Optional<Course> findByIdAndInstituteId(Long id, Long instituteId);
}