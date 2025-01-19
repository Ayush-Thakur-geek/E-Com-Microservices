package com.ecom.PaymentService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ecom.PaymentService.model.ErrorResponse;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PaymentServiceCustomException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceException(PaymentServiceCustomException ex) {
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorCode(ex.getCode())
                .errorMessage(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
    
}