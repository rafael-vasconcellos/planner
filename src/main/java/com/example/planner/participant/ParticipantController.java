package com.example.planner.participant;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.planner.participant.DTO.ConfirmParticipantRequestPayload;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/participants")
public class ParticipantController { 
    @Autowired
    private ParticipantService participantService;


    @PutMapping("/{participantId}/confirm")
    public ResponseEntity<Participant> confirm(@PathVariable UUID participantId, @RequestBody @Valid ConfirmParticipantRequestPayload payload) { 
        Participant participant = this.participantService.confirm(participantId, payload);
        return ResponseEntity.ok(participant);
    }

    @DeleteMapping("/{participantId}")
    public ResponseEntity<?> remove(@PathVariable UUID participantId) { 
        this.participantService.remove(participantId);
        return ResponseEntity.ok().build();
    }

}
