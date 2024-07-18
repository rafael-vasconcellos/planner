package com.example.planner.trip.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.planner.participant.Participant;
import com.example.planner.participant.ParticipantService;
import com.example.planner.participant.DTO.InviteParticipantRequestPayload;
import com.example.planner.participant.DTO.ParticipantResponseBody;
import com.example.planner.trip.Trip;
import com.example.planner.trip.TripRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/trips/{tripId}/participants")
public class TripParticipantsController { 
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ParticipantService participantService;


    @PostMapping("/invite")
    public ResponseEntity<Participant> createInvite(@PathVariable UUID tripId, @RequestBody @Valid InviteParticipantRequestPayload payload) { 
        Optional<Trip> query = this.tripRepository.findById(tripId);

        if (query.isPresent()) {
            Trip trip = query.get();
            Participant participant = this.participantService.register(payload.email(), trip);
            return ResponseEntity.ok(participant);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ParticipantResponseBody>> getParticipants(@PathVariable UUID tripId) { 
        List<ParticipantResponseBody> participants = this.participantService.getAllParticipants(tripId);
        return ResponseEntity.ok(participants);
    }

}
