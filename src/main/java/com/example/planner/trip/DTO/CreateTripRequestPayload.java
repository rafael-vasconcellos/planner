package com.example.planner.trip.DTO;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record CreateTripRequestPayload( 
    @NotEmpty String destination, 
    @NotEmpty String ownerEmail,
    @NotEmpty String ownerName,
    String startsAt, 
    String endsAt,
    List<String> invites
) {}
