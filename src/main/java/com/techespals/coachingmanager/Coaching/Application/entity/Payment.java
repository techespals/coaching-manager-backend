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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String receiptNumber;

    private Double amount;

    private LocalDate paymentDate;

    private String paymentMode;

    @ManyToOne
    private Student student;

    @ManyToOne
    @JoinColumn(name = "institute_id")
    private Institute institute;
}