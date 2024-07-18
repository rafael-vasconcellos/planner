package com.example.planner.link;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/links/{linkId}")
public class LinkController { 
    @Autowired
    private LinkService linkService;

    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable UUID linkId) { 
        this.linkService.remove(linkId);
        return ResponseEntity.ok().build();
    }
}
