package com.example.planner.participant;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.planner.participant.DTO.ParticipantResponse;
import com.example.planner.trip.Trip;

@Service
public class ParticipantService { 
    @Autowired
    private ParticipantRepository repository;

    public Participant register(String email, Trip trip) { 
        Participant participant = new Participant(email, trip);
        this.repository.save(participant);
        return participant;
    }

    public void registerList(List<String> invites, Trip trip) { 
        List<Participant> participants = invites.stream().map(email -> new Participant(email, trip)).toList();
        this.repository.saveAll(participants);
        this.sendMailToList(invites);
    }

    public List<ParticipantResponse> getAllParticipants(UUID tripId) { 
        List<Participant> participants = this.repository.findByTripId(tripId);
        List<ParticipantResponse> participantResponses = participants.stream().map(participant -> new ParticipantResponse(participant.getId(), participant.getName(), participant.getEmail(), participant.getIsConfirmed())).toList();
        return participantResponses;        
    }

    public void sendMailToList(List<String> inviteList) {}
}
