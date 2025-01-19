package com.ecom.OrderService.entity;

import java.time.Instant;

import com.ecom.OrderService.model.PaymentMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "ORDER_DETAILS")
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "PRODUCT_ID")
    private long productId;
    @Column(name = "QUANTITY")
    private long quantity;
    @Column(name = "ORDER_DATE")
    private Instant orderDate;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "TOTAL_AMOUNT")
    private long amount;
}
