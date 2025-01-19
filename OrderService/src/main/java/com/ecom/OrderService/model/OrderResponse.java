package com.ecom.OrderService.model;

import java.time.Instant;

import com.ecom.OrderService.external.response.PaymentResponse;
import com.ecom.OrderService.external.response.ProductResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private long orderId;
    private Instant orderDate;
    private String orderStatus;
    private long amount;

    private ProductResponse product;
    private PaymentResponse payment;
}
