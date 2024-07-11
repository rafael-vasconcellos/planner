package com.example.planner.participant.DTO;

public record ParticipantRequestPayload( 
    String name,
    String email
) { 

    public ParticipantRequestPayload(String email) { this(null, email); }
}
