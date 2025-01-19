package com.ecom.PaymentService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecom.PaymentService.entity.TransactionDetails;

@Repository
public interface PaymentRepository extends JpaRepository<TransactionDetails, Long> {
    Optional<TransactionDetails> findByOrderId(long orderId);
}
