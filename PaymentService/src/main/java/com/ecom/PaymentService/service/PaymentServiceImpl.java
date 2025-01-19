package com.ecom.PaymentService.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.ecom.PaymentService.entity.TransactionDetails;
import com.ecom.PaymentService.exception.PaymentServiceCustomException;
import com.ecom.PaymentService.model.PaymentMode;
import com.ecom.PaymentService.model.PaymentRequest;
import com.ecom.PaymentService.model.PaymentResponse;
import com.ecom.PaymentService.repository.PaymentRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Payment request received for order id: {}", paymentRequest.getOrderId());

        TransactionDetails transactionDetails = TransactionDetails.builder()
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentDate(Instant.now())
                .paymentStatus("SUCCESS")
                .build();

        return paymentRepository.save(transactionDetails).getId();
    }

    @Override
    public PaymentResponse getPaymentDetailByOrderId(long orderId) {
        log.info("Fetching payment details for order id: {}", orderId);

        TransactionDetails transactionDetails = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new PaymentServiceCustomException("Payment details not found for order id: " + orderId, "404"));

        return PaymentResponse.builder()
                .paymentId(transactionDetails.getId())
                .status(transactionDetails.getPaymentStatus())
                .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
                .amount(transactionDetails.getAmount())
                .orderId(transactionDetails.getOrderId())
                .paymentDate(transactionDetails.getPaymentDate())
                .build();
    }
    
}
