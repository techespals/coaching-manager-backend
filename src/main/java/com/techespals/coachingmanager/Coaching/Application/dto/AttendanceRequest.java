package com.techespals.coachingmanager.Coaching.Application.dto;



import com.techespals.coachingmanager.Coaching.Application.entity.AttendanceStatus;
import lombok.*;

@Getter
@Setter
public class AttendanceRequest {
    private Long studentId;
    private AttendanceStatus status;
}