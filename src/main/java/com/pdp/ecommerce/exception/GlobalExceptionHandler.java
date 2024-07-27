package com.pdp.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handler(TokenExpiredException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WrongConfirmationCodeException.class)
    public ResponseEntity<?> handler(WrongConfirmationCodeException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

    }
}
