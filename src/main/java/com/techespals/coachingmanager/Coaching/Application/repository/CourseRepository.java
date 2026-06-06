package com.techespals.coachingmanager.Coaching.Application.repository;



import com.techespals.coachingmanager.Coaching.Application.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}