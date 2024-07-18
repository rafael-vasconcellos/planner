package com.example.planner.participant.DTO;

import jakarta.validation.constraints.NotEmpty;


public record InviteParticipantRequestPayload( 
    @NotEmpty String email
) {}
