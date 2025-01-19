package com.ecom.OrderService.service;

import com.ecom.OrderService.model.OrderRequest;
import com.ecom.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
    OrderResponse getOrderDetails(long orderId);
}
