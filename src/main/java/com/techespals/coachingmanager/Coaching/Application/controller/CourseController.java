package com.techespals.coachingmanager.Coaching.Application.controller;


import com.techespals.coachingmanager.Coaching.Application.entity.Course;
import com.techespals.coachingmanager.Coaching.Application.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/courses")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CourseController {

    private final CourseRepository courseRepository;

    @PostMapping
    public Course addCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }

    @GetMapping
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @RequestBody Course course) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        existing.setCourseName(course.getCourseName());
        existing.setDescription(course.getDescription());
        existing.setFees(course.getFees());
        existing.setDuration(course.getDuration());

        return courseRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseRepository.deleteById(id);
        return "Course deleted successfully";
    }
}