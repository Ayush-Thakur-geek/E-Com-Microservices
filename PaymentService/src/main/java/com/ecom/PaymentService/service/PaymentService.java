package com.ecom.PaymentService.service;

import com.ecom.PaymentService.model.PaymentRequest;
import com.ecom.PaymentService.model.PaymentResponse;

public interface PaymentService {
    public long doPayment(PaymentRequest paymentRequest);
    public PaymentResponse getPaymentDetailByOrderId(long orderId);
}
