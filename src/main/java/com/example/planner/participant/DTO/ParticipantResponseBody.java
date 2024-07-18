package com.example.planner.participant.DTO;

import java.util.UUID;

public record ParticipantResponseBody(
    UUID id, 
    String name, 
    String email,
    Boolean isConfirmed
) {}
