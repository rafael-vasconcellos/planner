package com.example.planner.activity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.planner.activity.DTO.ActivityRequestPayload;
import com.example.planner.activity.DTO.ActivityResponseBody;
import com.example.planner.trip.Trip;

import jakarta.persistence.EntityNotFoundException;


@Service
public class ActivityService { 
    @Autowired
    private ActivityRepository activityRepository;


    public ActivityResponseBody register(ActivityRequestPayload payload, Trip trip) { 
        Activity activity = new Activity(payload.title(), payload.occursAt(), trip);
        this.activityRepository.save(activity);
        return new ActivityResponseBody(activity.getId(), activity.getTitle(), activity.getOccursAt());
    }

    public List<ActivityResponseBody> getAll(UUID tripId) { 
        List<ActivityResponseBody> activities = this.activityRepository.findByTripId(tripId)
        .stream()
        .map(activity -> new ActivityResponseBody(activity.getId(), activity.getTitle(), activity.getOccursAt()))
        .toList();
        return activities;
    }

    public void remove(UUID activityId) { 
        Optional<Activity> optionalActivity = this.activityRepository.findById(activityId);

        if (optionalActivity.isPresent()) { 
            Activity activity = optionalActivity.get();
            this.activityRepository.delete(activity);
            return;
        }

        throw new EntityNotFoundException();
    }

}
