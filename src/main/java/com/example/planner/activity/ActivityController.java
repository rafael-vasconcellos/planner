package com.example.planner.activity;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/activities/{activityId}")
public class ActivityController { 
    @Autowired
    private ActivityService activityService;


    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable UUID activityId) { 
        this.activityService.remove(activityId);
        return ResponseEntity.ok().build();
    }
}
