package com.example.planner.infra;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponseDTO { 
    private String title;
    private HttpStatus status;
    private String details;
    private String developerMessage;
    private LocalDateTime timestamp;
}
