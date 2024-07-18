package com.example.planner.activity.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityResponseBody( 
    UUID id,
    String title,
    LocalDateTime occursAt
) {}
