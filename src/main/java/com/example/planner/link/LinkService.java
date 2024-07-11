package com.example.planner.link;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.planner.link.DTO.LinkPayload;
import com.example.planner.trip.Trip;


@Service
public class LinkService { 
    @Autowired
    private LinkRepository repository;

    public Link add(LinkPayload payload, Trip trip) { 
        Link link = new Link(payload, trip);
        this.repository.save(link);
        return link;
    }

    public List<LinkPayload> getAll(UUID tripId) { 
        List<LinkPayload> response = this.repository.findByTripId(tripId)
        .stream()
        .map(link -> new LinkPayload(link.getUrl(), link.getTitle()))
        .toList();

        return response;
    }

}
