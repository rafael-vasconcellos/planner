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

import com.example.planner.activity.ActivityService;
import com.example.planner.activity.DTO.ActivityRequestPayload;
import com.example.planner.activity.DTO.ActivityResponse;
import com.example.planner.trip.Trip;
import com.example.planner.trip.TripRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/trips/{id}/activities")
public class TripActivitiesController { 
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ActivityService activityService;


    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(@PathVariable UUID id, @RequestBody @Valid ActivityRequestPayload payload) { 
        Optional<Trip> query = this.tripRepository.findById(id);

        if (query.isPresent()) { 
            Trip trip = query.get();
            ActivityResponse activity = this.activityService.register(payload, trip);
            return ResponseEntity.ok(activity);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> listActivities(@PathVariable UUID id) { 
        List<ActivityResponse> activities = this.activityService.getAll(id);
        return ResponseEntity.ok(activities);
    }
}
