package com.example.planner.participant.DTO;

import java.util.UUID;

public record ParticipantResponse(
    UUID id, 
    String name, 
    String email,
    Boolean isConfirmed
) {}
