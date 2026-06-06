package com.techespals.coachingmanager.Coaching.Application.dto;


import com.techespals.coachingmanager.Coaching.Application.entity.Role;
import lombok.*;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}