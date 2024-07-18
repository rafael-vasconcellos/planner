package com.example.planner.link.DTO;

import jakarta.validation.constraints.NotEmpty;

public record LinkPayload( 
    @NotEmpty String url,
    String title
) {}
