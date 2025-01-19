package com.ecom.OrderService.external.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ecom.OrderService.model.PaymentMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    
    private long orderId;
    private long amount;
    private String referenceNumber;
    private PaymentMode paymentMode;

}