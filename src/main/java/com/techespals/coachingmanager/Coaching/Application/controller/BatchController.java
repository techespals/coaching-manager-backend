package com.techespals.coachingmanager.Coaching.Application.controller;




import com.techespals.coachingmanager.Coaching.Application.entity.Batch;
import com.techespals.coachingmanager.Coaching.Application.entity.Course;
import com.techespals.coachingmanager.Coaching.Application.repository.BatchRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.CourseRepository;
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

    @PostMapping("/{courseId}")
    public Batch addBatch(@PathVariable Long courseId, @RequestBody Batch batch) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        batch.setCourse(course);
        return batchRepository.save(batch);
    }

    @GetMapping
    public List<Batch> getBatches() {
        return batchRepository.findAll();
    }

    @PutMapping("/{id}/course/{courseId}")
    public Batch updateBatch(
            @PathVariable Long id,
            @PathVariable Long courseId,
            @RequestBody Batch batch
    ) {
        Batch existing = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        existing.setBatchName(batch.getBatchName());
        existing.setTiming(batch.getTiming());
        existing.setCapacity(batch.getCapacity());
        existing.setTeacherName(batch.getTeacherName());
        existing.setCourse(course);

        return batchRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteBatch(@PathVariable Long id) {
        batchRepository.deleteById(id);
        return "Batch deleted successfully";
    }
}