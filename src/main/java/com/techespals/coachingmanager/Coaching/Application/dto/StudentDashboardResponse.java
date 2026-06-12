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
public class StudentDashboardResponse {

    private Student student;

    private int totalAttendance;
    private int presentDays;
    private int absentDays;
    private double attendancePercentage;

    private List<Payment> recentPayments;
}