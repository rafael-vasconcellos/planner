package com.example.planner.trip.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.planner.participant.ParticipantService;
import com.example.planner.trip.Trip;
import com.example.planner.trip.TripRepository;

@WebMvcTest(TripController.class)
class TripControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripRepository tripRepository;

    @MockBean
    private ParticipantService participantService;

    @Test
    void createTripShouldPersistAndReturnTrip() throws Exception {
        UUID tripId = UUID.randomUUID();
        Trip trip = new Trip(
            tripId,
            "Lisboa",
            LocalDateTime.parse("2026-10-01T10:00:00"),
            LocalDateTime.parse("2026-10-05T18:00:00"),
            true,
            "Maria",
            "maria@example.com"
        );

        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        String payload = """
            {
              "destination": "Lisboa",
              "ownerEmail": "maria@example.com",
              "ownerName": "Maria",
              "startsAt": "2026-10-01T10:00:00",
              "endsAt": "2026-10-05T18:00:00",
              "invites": ["ana@example.com", "joao@example.com"]
            }
            """;

        mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(tripId.toString()))
            .andExpect(jsonPath("$.destination").value("Lisboa"))
            .andExpect(jsonPath("$.ownerEmail").value("maria@example.com"));

        verify(participantService).registerList(List.of("ana@example.com", "joao@example.com"), trip);
    }

    @Test
    void createTripShouldReturnBadRequestWhenDestinationIsEmpty() throws Exception {
        String payload = """
            {
              "destination": "",
              "ownerEmail": "maria@example.com",
              "ownerName": "Maria",
              "startsAt": "2026-10-01T10:00:00",
              "endsAt": "2026-10-05T18:00:00",
              "invites": []
            }
            """;

        mockMvc.perform(post("/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$[0].field").value("destination"));
    }

    @Test
    void updateTripShouldReturnNotFoundWhenTripDoesNotExist() throws Exception {
        UUID tripId = UUID.randomUUID();
        when(tripRepository.findById(tripId)).thenReturn(Optional.empty());

        String payload = """
            {
              "destination": "Porto"
            }
            """;

        mockMvc.perform(put("/trips/{id}", tripId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isNotFound());
    }

    @Test
    void getUserTripsShouldValidateEmailParam() throws Exception {
        mockMvc.perform(get("/trips").param("email", ""))
            .andExpect(status().isBadRequest());
    }
}
