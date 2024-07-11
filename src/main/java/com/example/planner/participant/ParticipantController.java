package com.example.planner.participant;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.planner.participant.DTO.ParticipantRequestPayload;


@RestController
@RequestMapping("/participants")
public class ParticipantController { 
    @Autowired
    private ParticipantRepository participantRepository;


    @PutMapping("/{participantId}/confirm")
    public ResponseEntity<Participant> confirm(@PathVariable UUID participantId, @RequestBody ParticipantRequestPayload payload) { 
        Optional<Participant> query = this.participantRepository.findById(participantId);

        if (query.isPresent() && !payload.name().isEmpty()) { 
            Participant participant = query.get();
            participant.setName(payload.name());
            participant.setIsConfirmed(true);
            this.participantRepository.save(participant);
            return ResponseEntity.ok(participant);

        } else if (payload.name().isEmpty()) { return ResponseEntity.badRequest().build(); }

        return ResponseEntity.notFound().build();
    }
}
