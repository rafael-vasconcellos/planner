package com.example.planner;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping
public class PlannerController {
    @GetMapping
    public ResponseEntity<?> home() {
        return ResponseEntity.ok("Hello world!");
    }
}