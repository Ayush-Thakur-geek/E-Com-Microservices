package com.ecom.OrderService.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecom.OrderService.entity.Order;

@Repository
public interface OrderServiceRepository extends JpaRepository<Order, Long> {}
