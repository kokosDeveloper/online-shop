package com.kokos.onlineshop.exception;

import jakarta.validation.constraints.NotBlank;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
