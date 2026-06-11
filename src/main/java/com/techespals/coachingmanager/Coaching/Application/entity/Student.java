package com.techespals.coachingmanager.Coaching.Application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;
    private String parentPhone;

    private Double totalFees;
    private Double paidFees;
    private Double remainingFees;

    @Enumerated(EnumType.STRING)
    private FeeStatus feeStatus;

    private LocalDate joiningDate;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "institute_id")
    private Institute institute;
}