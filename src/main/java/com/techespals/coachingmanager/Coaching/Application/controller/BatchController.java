package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.entity.Batch;
import com.techespals.coachingmanager.Coaching.Application.entity.Course;
import com.techespals.coachingmanager.Coaching.Application.entity.Institute;
import com.techespals.coachingmanager.Coaching.Application.entity.User;
import com.techespals.coachingmanager.Coaching.Application.repository.BatchRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.CourseRepository;
import com.techespals.coachingmanager.Coaching.Application.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/batches")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BatchController {

    private final BatchRepository batchRepository;
    private final CourseRepository courseRepository;
    private final CurrentUserService currentUserService;

    @PostMapping("/{courseId}")
    public Batch addBatch(@PathVariable Long courseId, @RequestBody Batch batch) {
        User currentUser = currentUserService.getCurrentUser();
        Institute institute = currentUser.getInstitute();
        Long instituteId = currentUserService.getCurrentInstituteId();

        Course course = courseRepository.findByIdAndInstituteId(courseId, instituteId)
                .orElseThrow(() -> new RuntimeException("Course not found for this institute"));

        batch.setId(null);
        batch.setCourse(course);
        batch.setInstitute(institute);

        return batchRepository.save(batch);
    }

    @GetMapping
    public List<Batch> getBatches() {
        Long instituteId = currentUserService.getCurrentInstituteId();
        return batchRepository.findByInstituteId(instituteId);
    }

    @PutMapping("/{id}/course/{courseId}")
    public Batch updateBatch(
            @PathVariable Long id,
            @PathVariable Long courseId,
            @RequestBody Batch batch
    ) {
        Long instituteId = currentUserService.getCurrentInstituteId();

        Batch existing = batchRepository.findByIdAndInstituteId(id, instituteId)
                .orElseThrow(() -> new RuntimeException("Batch not found for this institute"));

        Course course = courseRepository.findByIdAndInstituteId(courseId, instituteId)
                .orElseThrow(() -> new RuntimeException("Course not found for this institute"));

        existing.setBatchName(batch.getBatchName());
        existing.setTiming(batch.getTiming());
        existing.setCapacity(batch.getCapacity());
        existing.setTeacherName(batch.getTeacherName());
        existing.setCourse(course);

        return batchRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteBatch(@PathVariable Long id) {
        Long instituteId = currentUserService.getCurrentInstituteId();

        Batch existing = batchRepository.findByIdAndInstituteId(id, instituteId)
                .orElseThrow(() -> new RuntimeException("Batch not found for this institute"));

        batchRepository.delete(existing);

        return "Batch deleted successfully";
    }
}