package com.techespals.coachingmanager.Coaching.Application.dto;



import com.techespals.coachingmanager.Coaching.Application.entity.Payment;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private int totalStudents;
    private int totalCourses;
    private int totalBatches;

    private double totalCollection;
    private double todayCollection;
    private double pendingFees;

    private int pendingStudents;

    private List<Student> latestStudents;
    private List<Payment> latestPayments;
}