package com.example.planner.infra;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.persistence.EntityNotFoundException;


// extends ResponseEntityExceptionHandler
@ControllerAdvice
public class MyExceptionHandler { 

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex) { 
        System.out.println(ex.getClass().getName());
        List<FieldError> errors = ex.getFieldErrors();
        List<ErrorData> mappedErrors = errors.stream().map(ErrorData::new).toList();
        return ResponseEntity.badRequest().body(mappedErrors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFound(EntityNotFoundException ex) { 
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> noResourceFound(NoResourceFoundException ex) { 
        return ResponseEntity.notFound().build();
    }

    public record ErrorData(String field, String message) { 
        public ErrorData(FieldError fieldError) { 
            this(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
