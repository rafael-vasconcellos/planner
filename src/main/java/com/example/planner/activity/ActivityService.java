package com.example.planner.activity;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.planner.activity.DTO.ActivityRequestPayload;
import com.example.planner.activity.DTO.ActivityResponse;
import com.example.planner.trip.Trip;


@Service
public class ActivityService { 
    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponse register(ActivityRequestPayload payload, Trip trip) { 
        Activity activity = new Activity(payload.title(), payload.occursAt(), trip);
        this.activityRepository.save(activity);
        return new ActivityResponse(activity.getId(), activity.getTitle(), activity.getOccursAt());
    }

    public List<ActivityResponse> getAll(UUID tripId) { 
        List<ActivityResponse> activities = this.activityRepository.findByTripId(tripId)
        .stream()
        .map(activity -> new ActivityResponse(activity.getId(), activity.getTitle(), activity.getOccursAt()))
        .toList();
        return activities;
    }
}
