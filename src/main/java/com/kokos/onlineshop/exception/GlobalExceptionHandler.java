package com.kokos.onlineshop.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleException(AlreadyExistsException e){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(EntityNotFoundException e){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ExceptionResponse> handleException(InsufficientStockException e){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException e){
        ExceptionResponse response = new ExceptionResponse();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            response.getFieldErrors().put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleException(IllegalStateException e){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(exceptionResponse);
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionResponse> handleException(IOException e){
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.badRequest().body(exceptionResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e){
        e.printStackTrace();
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.internalServerError().body(exceptionResponse);
    }

}
