package com.techespals.coachingmanager.Coaching.Application.service;

import com.techespals.coachingmanager.Coaching.Application.dto.StudentRequest;
import com.techespals.coachingmanager.Coaching.Application.entity.*;
import com.techespals.coachingmanager.Coaching.Application.repository.BatchRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.CourseRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.StudentRepository;
import com.techespals.coachingmanager.Coaching.Application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final BatchRepository batchRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    public Student addStudent(StudentRequest request) {

        User currentUser = currentUserService.getCurrentUser();
        Long instituteId = currentUserService.getCurrentInstituteId();
        Institute institute = currentUser.getInstitute();

        Course course = courseRepository.findByIdAndInstituteId(request.getCourseId(), instituteId)
                .orElseThrow(() -> new RuntimeException("Course not found for this institute"));

        Batch batch = batchRepository.findByIdAndInstituteId(request.getBatchId(), instituteId)
                .orElseThrow(() -> new RuntimeException("Batch not found for this institute"));

        double remaining = request.getTotalFees() - request.getPaidFees();

        Student student = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .parentPhone(request.getParentPhone())
                .totalFees(request.getTotalFees())
                .paidFees(request.getPaidFees())
                .remainingFees(remaining)
                .feeStatus(getFeeStatus(request.getTotalFees(), request.getPaidFees()))
                .joiningDate(LocalDate.now())
                .course(course)
                .batch(batch)
                .institute(institute)
                .build();

        Student savedStudent = studentRepository.save(student);

        if (!userRepository.existsByEmail(request.getPhone())) {
            User user = User.builder()
                    .name(request.getName())
                    .email(request.getPhone())
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.STUDENT)
                    .institute(institute)
                    .build();

            userRepository.save(user);
        }

        return savedStudent;
    }

    public List<Student> getAllStudents() {
        Long instituteId = currentUserService.getCurrentInstituteId();
        return studentRepository.findByInstituteId(instituteId);
    }

    public Student getStudentById(Long id) {
        Long instituteId = currentUserService.getCurrentInstituteId();

        return studentRepository.findByIdAndInstituteId(id, instituteId)
                .orElseThrow(() -> new RuntimeException("Student not found for this institute"));
    }

    public Student updateStudent(Long id, StudentRequest request) {

        Long instituteId = currentUserService.getCurrentInstituteId();

        Student student = getStudentById(id);

        Course course = courseRepository.findByIdAndInstituteId(request.getCourseId(), instituteId)
                .orElseThrow(() -> new RuntimeException("Course not found for this institute"));

        Batch batch = batchRepository.findByIdAndInstituteId(request.getBatchId(), instituteId)
                .orElseThrow(() -> new RuntimeException("Batch not found for this institute"));

        double remaining = request.getTotalFees() - request.getPaidFees();

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setParentPhone(request.getParentPhone());
        student.setTotalFees(request.getTotalFees());
        student.setPaidFees(request.getPaidFees());
        student.setRemainingFees(remaining);
        student.setFeeStatus(getFeeStatus(request.getTotalFees(), request.getPaidFees()));
        student.setCourse(course);
        student.setBatch(batch);

        return studentRepository.save(student);
    }

    public String deleteStudent(Long id) {
        Student student = getStudentById(id);

        userRepository.findByEmail(student.getPhone())
                .ifPresent(userRepository::delete);

        studentRepository.delete(student);

        return "Student deleted successfully";
    }

    public List<Student> getUnpaidStudents() {
        Long instituteId = currentUserService.getCurrentInstituteId();
        return studentRepository.findByFeeStatusAndInstituteId(FeeStatus.UNPAID, instituteId);
    }

    public List<Student> getPartialFeeStudents() {
        Long instituteId = currentUserService.getCurrentInstituteId();
        return studentRepository.findByFeeStatusAndInstituteId(FeeStatus.PARTIAL, instituteId);
    }

    public List<Student> sortByName() {
        List<Student> students = getAllStudents();
        students.sort(Comparator.comparing(Student::getName));
        return students;
    }

    public List<Student> sortByRemainingFees() {
        List<Student> students = getAllStudents();
        students.sort(Comparator.comparing(Student::getRemainingFees).reversed());
        return students;
    }

    private FeeStatus getFeeStatus(Double totalFees, Double paidFees) {
        if (paidFees == 0) return FeeStatus.UNPAID;
        if (paidFees < totalFees) return FeeStatus.PARTIAL;
        return FeeStatus.PAID;
    }
}