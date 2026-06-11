package com.techespals.coachingmanager.Coaching.Application.dto;

import com.techespals.coachingmanager.Coaching.Application.entity.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AttendanceRequest {

    private Long studentId;
    private Long batchId;
    private LocalDate date;
    private AttendanceStatus status;
}