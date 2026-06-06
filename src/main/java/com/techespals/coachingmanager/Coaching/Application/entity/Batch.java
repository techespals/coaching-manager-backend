package com.techespals.coachingmanager.Coaching.Application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batchName;

    private String timing;

    private Integer capacity;

    private String teacherName;

    @ManyToOne
    private Course course;
}