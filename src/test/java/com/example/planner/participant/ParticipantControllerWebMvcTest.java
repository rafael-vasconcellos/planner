package com.example.planner.participant;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.planner.trip.Trip;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(ParticipantController.class)
class ParticipantControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParticipantService participantService;

    @Test
    void confirmShouldReturnUpdatedParticipant() throws Exception {
        UUID participantId = UUID.randomUUID();
        Participant participant = new Participant(participantId, "Ana", "ana@example.com", true, new Trip());

        when(participantService.confirm(org.mockito.ArgumentMatchers.eq(participantId), org.mockito.ArgumentMatchers.any()))
            .thenReturn(participant);

        String payload = """
            {
              "name": "Ana"
            }
            """;

        mockMvc.perform(put("/participants/{participantId}/confirm", participantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(participantId.toString()))
            .andExpect(jsonPath("$.name").value("Ana"))
            .andExpect(jsonPath("$.isConfirmed").value(true));
    }

    @Test
    void confirmShouldReturnNotFoundWhenParticipantDoesNotExist() throws Exception {
        UUID participantId = UUID.randomUUID();
        when(participantService.confirm(org.mockito.ArgumentMatchers.eq(participantId), org.mockito.ArgumentMatchers.any()))
            .thenThrow(new EntityNotFoundException());

        String payload = """
            {
              "name": "Ana"
            }
            """;

        mockMvc.perform(put("/participants/{participantId}/confirm", participantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isNotFound());
    }

    @Test
    void confirmShouldReturnBadRequestWhenNameIsEmpty() throws Exception {
        UUID participantId = UUID.randomUUID();

        String payload = """
            {
              "name": ""
            }
            """;

        mockMvc.perform(put("/participants/{participantId}/confirm", participantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$[0].field").value("name"));
    }

    @Test
    void removeShouldReturnNotFoundWhenParticipantDoesNotExist() throws Exception {
        UUID participantId = UUID.randomUUID();
        org.mockito.Mockito.doThrow(new EntityNotFoundException())
            .when(participantService)
            .remove(participantId);

        mockMvc.perform(delete("/participants/{participantId}", participantId))
            .andExpect(status().isNotFound());
    }
}
