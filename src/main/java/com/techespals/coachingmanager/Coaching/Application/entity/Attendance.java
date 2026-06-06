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
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    @ManyToOne
    private Student student;
}