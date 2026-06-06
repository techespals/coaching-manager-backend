package com.techespals.coachingmanager.Coaching.Application.dto;

import lombok.*;

@Getter
@Setter
public class PaymentRequest {
    private Long studentId;
    private Double amount;
    private String paymentMode;
}