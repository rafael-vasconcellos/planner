package com.example.planner.link.DTO;

import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;

public record LinkResponseBody( 
    @NotEmpty UUID id,
    @NotEmpty String url,
    @NotEmpty String title
) {}
