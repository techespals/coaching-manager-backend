package com.techespals.coachingmanager.Coaching.Application.dto;

import lombok.*;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}