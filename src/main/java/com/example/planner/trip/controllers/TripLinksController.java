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

import com.example.planner.link.Link;
import com.example.planner.link.LinkService;
import com.example.planner.link.DTO.CreateLinkRequestPayload;
import com.example.planner.link.DTO.LinkResponseBody;
import com.example.planner.trip.Trip;
import com.example.planner.trip.TripRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("trips/{tripId}/links")
public class TripLinksController { 
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private LinkService linkService;


    @PostMapping
    public ResponseEntity<Link> addLink(@PathVariable UUID tripId, @RequestBody @Valid CreateLinkRequestPayload payload) { 
        Optional<Trip> tripOptional = this.tripRepository.findById(tripId);

        if (tripOptional.isPresent()) { 
            Trip trip = tripOptional.get();
            Link link = this.linkService.add(payload, trip);
            return ResponseEntity.ok(link);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<LinkResponseBody>> listLinks(@PathVariable UUID tripId) { 
        List<LinkResponseBody> list = this.linkService.getAll(tripId);
        return ResponseEntity.ok(list);
    }

}
