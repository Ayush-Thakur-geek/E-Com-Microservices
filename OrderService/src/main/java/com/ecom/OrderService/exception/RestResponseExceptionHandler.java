package com.ecom.OrderService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecom.OrderService.external.response.ErrorResponse;

@ControllerAdvice
public class RestResponseExceptionHandler {
    
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleOrderServiceException(CustomException ex) {
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorMessage(ex.getMessage())
                .errorCode(ex.getErrorCode())
                .build(), HttpStatus.valueOf(ex.getStatus()));
    }

}
