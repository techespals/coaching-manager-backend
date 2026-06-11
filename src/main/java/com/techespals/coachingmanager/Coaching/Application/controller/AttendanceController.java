package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.dto.AttendanceRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.Attendance;
import com.techespals.coachingmanager.Coaching.Application.entity.Batch;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import com.techespals.coachingmanager.Coaching.Application.repository.AttendanceRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.BatchRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.StudentRepository;
import com.techespals.coachingmanager.Coaching.Application.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/owner/attendance")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AttendanceController {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final BatchRepository batchRepository;
    private final CurrentUserService currentUserService;

    @PostMapping
    public Attendance markAttendance(@RequestBody AttendanceRequest request) {
        Long instituteId = currentUserService.getCurrentInstituteId();
        LocalDate attendanceDate = getAttendanceDate(request.getDate());

        Student student = getStudent(request.getStudentId(), instituteId);
        Batch batch = getBatch(request.getBatchId(), instituteId);

        validateStudentBatch(student, batch);

        Attendance attendance = getOrCreateAttendance(student, batch, attendanceDate, instituteId);
        attendance.setStatus(request.getStatus());

        return attendanceRepository.save(attendance);
    }

    @PostMapping("/bulk")
    public List<Attendance> markBulkAttendance(@RequestBody List<AttendanceRequest> requests) {
        Long instituteId = currentUserService.getCurrentInstituteId();

        if (requests == null || requests.isEmpty()) {
            throw new RuntimeException("Attendance list cannot be empty");
        }

        List<Attendance> attendanceList = new ArrayList<>();

        for (AttendanceRequest request : requests) {
            LocalDate attendanceDate = getAttendanceDate(request.getDate());

            Student student = getStudent(request.getStudentId(), instituteId);
            Batch batch = getBatch(request.getBatchId(), instituteId);

            validateStudentBatch(student, batch);

            Attendance attendance = getOrCreateAttendance(
                    student,
                    batch,
                    attendanceDate,
                    instituteId
            );

            attendance.setStatus(request.getStatus());
            attendanceList.add(attendance);
        }

        return attendanceRepository.saveAll(attendanceList);
    }

    @GetMapping("/batch/{batchId}")
    public List<Attendance> getBatchAttendance(
            @PathVariable Long batchId,
            @RequestParam(required = false) String date
    ) {
        Long instituteId = currentUserService.getCurrentInstituteId();

        LocalDate attendanceDate = date != null
                ? LocalDate.parse(date)
                : LocalDate.now();

        return attendanceRepository.findByBatchIdAndDateAndInstituteId(
                batchId,
                attendanceDate,
                instituteId
        );
    }

    @GetMapping("/batch/{batchId}/all")
    public List<Attendance> getBatchAttendanceHistory(@PathVariable Long batchId) {
        Long instituteId = currentUserService.getCurrentInstituteId();

        return attendanceRepository.findByBatchIdAndInstituteId(batchId, instituteId);
    }

    @GetMapping("/student/{studentId}")
    public List<Attendance> getStudentAttendance(@PathVariable Long studentId) {
        Long instituteId = currentUserService.getCurrentInstituteId();

        return attendanceRepository.findByStudentIdAndInstituteId(studentId, instituteId);
    }

    private LocalDate getAttendanceDate(LocalDate date) {
        return date != null ? date : LocalDate.now();
    }

    private Student getStudent(Long studentId, Long instituteId) {
        return studentRepository
                .findByIdAndInstituteId(studentId, instituteId)
                .orElseThrow(() -> new RuntimeException("Student not found for this institute"));
    }

    private Batch getBatch(Long batchId, Long instituteId) {
        return batchRepository
                .findByIdAndInstituteId(batchId, instituteId)
                .orElseThrow(() -> new RuntimeException("Batch not found for this institute"));
    }

    private void validateStudentBatch(Student student, Batch batch) {
        if (student.getBatch() == null || !student.getBatch().getId().equals(batch.getId())) {
            throw new RuntimeException("Student does not belong to selected batch");
        }
    }

    private Attendance getOrCreateAttendance(
            Student student,
            Batch batch,
            LocalDate attendanceDate,
            Long instituteId
    ) {
        return attendanceRepository
                .findByStudentIdAndDateAndInstituteId(
                        student.getId(),
                        attendanceDate,
                        instituteId
                )
                .orElse(
                        Attendance.builder()
                                .student(student)
                                .batch(batch)
                                .institute(student.getInstitute())
                                .date(attendanceDate)
                                .build()
                );
    }
}