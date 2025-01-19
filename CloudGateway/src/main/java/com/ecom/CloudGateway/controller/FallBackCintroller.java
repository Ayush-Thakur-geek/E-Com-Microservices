package com.ecom.CloudGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackCintroller {
    
    @GetMapping("/orderServiceFallback")
    public String orderServiceFallBack(){
        return "Order Service is taking longer than expected." +
                "Please try again later";
    }

    @GetMapping("/productServiceFallback")
    public String productServiceFallBack(){
        return "Product Service is taking longer than expected." +
                "Please try again later";
    }

    @GetMapping("/paymentServiceFallback")
    public String paymentServiceFallBack(){
        return "Payment Service is taking longer than expected." +
                "Please try again later";
    }
}
