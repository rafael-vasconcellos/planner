package com.example.planner.trip.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.planner.participant.ParticipantService;
import com.example.planner.trip.Trip;
import com.example.planner.trip.TripRepository;
import com.example.planner.trip.DTO.CreateTripRequestPayload;
import com.example.planner.trip.DTO.TripRequestPayload;

import jakarta.validation.constraints.NotEmpty;


@RestController
@RequestMapping("/trips")
public class TripController { 
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ParticipantService participantService;


    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody CreateTripRequestPayload payload) {
        Trip trip = new Trip(payload);
        this.tripRepository.save(trip);
        this.participantService.registerList(payload.invites(), trip);

        return ResponseEntity.ok(trip);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable UUID id) { 
        Optional<Trip> trip = this.tripRepository.findById(id);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) { 
        Optional<Trip> query = this.tripRepository.findById(id);

        if (query.isPresent()) { 
            Trip trip = query.get();
            if (payload.destination() != null) { trip.setDestination(payload.destination()); }
            if (payload.ownerName() != null) { trip.setOwnerName(payload.ownerName()); }
            if (payload.startsAt() != null) { trip.setStartsAt(LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME)); }
            if (payload.endsAt() != null) { trip.setEndsAt(LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME)); }

            this.tripRepository.save(trip);
            return ResponseEntity.ok(trip);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @Validated
    public ResponseEntity<List<Trip>> getUserTrips(@RequestParam @NotEmpty String email) { 
        List<Trip> trips = this.tripRepository.findByOwnerEmail(email);
        return ResponseEntity.ok(trips);
    }


    /* ModelAttribute example
    @GetMapping("/search")
    public String search(@Valid @ModelAttribute SearchCriteria criteria) {
        return "param1: " + criteria.getParam1() + ", param2: " + criteria.getParam2();
    } */

}
