package com.example.planner.link.DTO;

import jakarta.validation.constraints.NotEmpty;


public record CreateLinkRequestPayload( 
    @NotEmpty String title,
    @NotEmpty String url
) {}
