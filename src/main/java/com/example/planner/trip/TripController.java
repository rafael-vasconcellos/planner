package com.example.planner.trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.planner.activity.ActivityService;
import com.example.planner.activity.DTO.ActivityRequestPayload;
import com.example.planner.activity.DTO.ActivityResponse;
import com.example.planner.participant.Participant;
import com.example.planner.participant.ParticipantService;
import com.example.planner.participant.DTO.ParticipantRequestPayload;
import com.example.planner.participant.DTO.ParticipantResponse;
import com.example.planner.trip.DTO.TripRequestPayload;


@RestController
@RequestMapping("/trips")
public class TripController { 
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private ActivityService activityService;


    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody TripRequestPayload payload) {
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


    @PostMapping("/{id}/invite")
    public ResponseEntity<Participant> createInvite(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) { 
        Optional<Trip> query = this.tripRepository.findById(id);

        if (query.isPresent() && !payload.email().isEmpty()) {
            Trip trip = query.get();
            Participant participant = this.participantService.register(payload.email(), trip);
            return ResponseEntity.ok(participant);

        } else if(payload.email().isEmpty()) { return ResponseEntity.badRequest().build(); }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable UUID id) { 
        List<ParticipantResponse> participants = this.participantService.getAllParticipants(id);

        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> createActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) { 
        Optional<Trip> query = this.tripRepository.findById(id);

        if (query.isPresent()) { 
            Trip trip = query.get();
            ActivityResponse activity = this.activityService.register(payload, trip);
            return ResponseEntity.ok(activity);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityResponse>> listActivities(@PathVariable UUID id) { 
        List<ActivityResponse> activities = this.activityService.getAll(id);
        return ResponseEntity.ok(activities);
    }
}
