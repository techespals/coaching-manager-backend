package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.entity.Course;
import com.techespals.coachingmanager.Coaching.Application.entity.Institute;
import com.techespals.coachingmanager.Coaching.Application.entity.User;
import com.techespals.coachingmanager.Coaching.Application.repository.CourseRepository;
import com.techespals.coachingmanager.Coaching.Application.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/courses")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CourseController {

    private final CourseRepository courseRepository;
    private final CurrentUserService currentUserService;

    @PostMapping
    public Course addCourse(@RequestBody Course course) {
        User currentUser = currentUserService.getCurrentUser();
        Institute institute = currentUser.getInstitute();

        course.setId(null);
        course.setInstitute(institute);

        return courseRepository.save(course);
    }

    @GetMapping
    public List<Course> getCourses() {
        Long instituteId = currentUserService.getCurrentInstituteId();
        return courseRepository.findByInstituteId(instituteId);
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @RequestBody Course course) {
        Long instituteId = currentUserService.getCurrentInstituteId();

        Course existing = courseRepository.findByIdAndInstituteId(id, instituteId)
                .orElseThrow(() -> new RuntimeException("Course not found for this institute"));

        existing.setCourseName(course.getCourseName());
        existing.setDescription(course.getDescription());
        existing.setFees(course.getFees());
        existing.setDuration(course.getDuration());

        return courseRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteCourse(@PathVariable Long id) {
        Long instituteId = currentUserService.getCurrentInstituteId();

        Course existing = courseRepository.findByIdAndInstituteId(id, instituteId)
                .orElseThrow(() -> new RuntimeException("Course not found for this institute"));

        courseRepository.delete(existing);

        return "Course deleted successfully";
    }
}