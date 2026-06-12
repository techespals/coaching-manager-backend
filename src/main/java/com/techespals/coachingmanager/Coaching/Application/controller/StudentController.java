package com.techespals.coachingmanager.Coaching.Application.controller;

import com.techespals.coachingmanager.Coaching.Application.dto.StudentRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import com.techespals.coachingmanager.Coaching.Application.service.CurrentUserService;
import com.techespals.coachingmanager.Coaching.Application.service.StudentExcelExportService;

import com.techespals.coachingmanager.Coaching.Application.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/owner/students")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StudentController {

    private final StudentService studentService;
    private final StudentExcelExportService studentExcelExportService;

    private final CurrentUserService currentUserService;

    @PostMapping
    public Student addStudent(@RequestBody StudentRequest request) {
        return studentService.addStudent(request);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody StudentRequest request) {
        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

    @PostMapping("/{id}/photo")
    public Student uploadStudentPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return studentService.uploadStudentPhoto(id, file);
    }



    @GetMapping("/unpaid")
    public List<Student> unpaidStudents() {
        return studentService.getUnpaidStudents();
    }

    @GetMapping("/partial")
    public List<Student> partialFeeStudents() {
        return studentService.getPartialFeeStudents();
    }

    @GetMapping("/sort/name")
    public List<Student> sortByName() {
        return studentService.sortByName();
    }

    @GetMapping("/sort/remaining-fees")
    public List<Student> sortByRemainingFees() {
        return studentService.sortByRemainingFees();
    }

    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportStudents() {

        Long instituteId = currentUserService.getCurrentInstituteId();

        List<Student> students = studentService.getAllStudentsByInstitute(instituteId);

        ByteArrayInputStream excel = studentExcelExportService.exportStudents(students);

        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=students.xlsx"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .body(new InputStreamResource(excel));
    }
}