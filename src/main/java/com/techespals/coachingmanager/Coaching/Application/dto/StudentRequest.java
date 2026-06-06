package com.techespals.coachingmanager.Coaching.Application.dto;


import lombok.*;

@Getter
@Setter
public class StudentRequest {
    private String name;
    private String email;
    private String phone;
    private String parentPhone;
    private Double totalFees;
    private Double paidFees;
    private Long courseId;
    private Long batchId;
}