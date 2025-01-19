package com.ecom.PaymentService.exception;

import lombok.Data;

@Data
public class PaymentServiceCustomException extends RuntimeException {
    private String code;
    public PaymentServiceCustomException(String message, String code) {
        super(message);
        this.code = code;
    }
}
