package com.example.planner.participant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.planner.participant.DTO.ConfirmParticipantRequestPayload;
import com.example.planner.participant.DTO.ParticipantResponseBody;
import com.example.planner.trip.Trip;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ParticipantService { 
    @Autowired
    private ParticipantRepository participantRepository;

    public Participant register(String email, Trip trip) { 
        Participant participant = new Participant(email, trip);
        this.participantRepository.save(participant);
        return participant;
    }

    public void registerList(List<String> invites, Trip trip) { 
        List<Participant> participants = invites.stream().map(email -> new Participant(email, trip)).toList();
        this.participantRepository.saveAll(participants);
        this.sendMailToList(invites);
    }

    public List<ParticipantResponseBody> getAllParticipants(UUID tripId) { 
        List<Participant> participants = this.participantRepository.findByTripId(tripId);
        List<ParticipantResponseBody> participantResponses = participants.stream().map(participant -> new ParticipantResponseBody(participant.getId(), participant.getName(), participant.getEmail(), participant.getIsConfirmed())).toList();
        return participantResponses;        
    }

    public Participant confirm(UUID participantId, ConfirmParticipantRequestPayload payload) { 
        Optional<Participant> query = this.participantRepository.findById(participantId);

        if (query.isPresent()) { 
            Participant participant = query.get();
            participant.setName(payload.name());
            participant.setIsConfirmed(true);
            this.participantRepository.save(participant);
            return participant;
        }

        throw new EntityNotFoundException();
    }

    public void remove(UUID participantId) { 
        Optional<Participant> optionalParticipant = this.participantRepository.findById(participantId);

        if (optionalParticipant.isPresent()) { 
            Participant participant = optionalParticipant.get();
            this.participantRepository.delete(participant);
            return;
        }

        throw new EntityNotFoundException();
    }

    public void sendMailToList(List<String> inviteList) {}
}
