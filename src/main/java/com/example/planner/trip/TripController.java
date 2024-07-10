package com.example.planner.trip;

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

import com.example.planner.trip.DTO.TripRequestPayload;


@RestController
@RequestMapping("/trips")
public class TripController { 
    @Autowired
    private TripRepository repository;
    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody TripRequestPayload payload) {
        Trip trip = new Trip(payload);
        this.repository.save(trip);

        return ResponseEntity.ok(trip);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable UUID id) { 
        Optional<Trip> trip = this.repository.findById(id);

        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
