package com.example.planner.activity.DTO;

import jakarta.validation.constraints.NotEmpty;

public record ActivityRequestPayload( 
    @NotEmpty
    String title,
    @NotEmpty
    String occursAt
) {}