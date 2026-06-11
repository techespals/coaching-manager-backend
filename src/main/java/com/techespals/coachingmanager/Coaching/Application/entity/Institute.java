package com.techespals.coachingmanager.Coaching.Application.entity;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "institutes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Institute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String instituteName;

    private String ownerName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    private Boolean active;
}