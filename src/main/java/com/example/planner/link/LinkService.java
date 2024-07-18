package com.example.planner.link;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.planner.link.DTO.CreateLinkRequestPayload;
import com.example.planner.link.DTO.LinkResponseBody;
import com.example.planner.trip.Trip;

import jakarta.persistence.EntityNotFoundException;


@Service
public class LinkService { 
    @Autowired
    private LinkRepository linkRepository;


    public Link add(CreateLinkRequestPayload payload, Trip trip) { 
        Link link = new Link(payload, trip);
        this.linkRepository.save(link);
        return link;
    }

    public List<LinkResponseBody> getAll(UUID tripId) { 
        List<LinkResponseBody> response = this.linkRepository.findByTripId(tripId)
        .stream()
        .map(link -> new LinkResponseBody(link.getId(), link.getUrl(), link.getTitle()))
        .toList();

        return response;
    }

    public void remove(UUID linkId) { 
        Optional<Link> optionalLink = this.linkRepository.findById(linkId);

        if (optionalLink.isPresent()) { 
            Link link = optionalLink.get();
            this.linkRepository.delete(link);
            return;
        }

        throw new EntityNotFoundException();
    }

}
