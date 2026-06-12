package com.techespals.coachingmanager.Coaching.Application.dto;

import com.techespals.coachingmanager.Coaching.Application.entity.Payment;
import com.techespals.coachingmanager.Coaching.Application.entity.Student;
import lombok.*;

import java.util.List;
import java.util.Map;

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

    private int todayPresent;
    private int todayAbsent;
    private double todayAttendancePercentage;

    private Map<String, Double> monthlyCollection;

    private List<Student> latestStudents;
    private List<Payment> latestPayments;
}