package com.techespals.coachingmanager.Coaching.Application.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInstituteRequest {

    private String instituteName;
    private String ownerName;
    private String email;
    private String phone;

    private String adminName;
    private String adminEmail;
    private String adminPassword;
}