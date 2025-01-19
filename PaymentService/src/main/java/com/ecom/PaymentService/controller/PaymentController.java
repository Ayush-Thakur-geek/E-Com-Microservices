package com.ecom.PaymentService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.PaymentService.model.PaymentRequest;
import com.ecom.PaymentService.model.PaymentResponse;
import com.ecom.PaymentService.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest) {
        return new ResponseEntity<>(paymentService.doPayment(paymentRequest), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetailByOrderId(@PathVariable long orderId) {
        return new ResponseEntity<>(paymentService.getPaymentDetailByOrderId(orderId), HttpStatus.OK);
    }
    
}
