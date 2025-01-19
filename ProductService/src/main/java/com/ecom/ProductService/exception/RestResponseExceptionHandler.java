package com.ecom.ProductService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ecom.ProductService.model.ErrorResponse;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductServiceCustomException.class)
    public ResponseEntity<ErrorResponse> handleProductServiceException(ProductServiceCustomException ex) {
        return new ResponseEntity<>(new ErrorResponse().builder()
                .errorCode(ex.getErrorCode())
                .errorMessage(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
    
}
