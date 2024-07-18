package com.example.planner.trip.DTO;


public record TripRequestPayload(
    String destination, 
    String startsAt, 
    String endsAt,
    String ownerName,
    String ownerEmail
) {}
