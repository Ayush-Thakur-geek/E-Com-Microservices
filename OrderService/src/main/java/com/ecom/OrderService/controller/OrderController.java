package com.ecom.OrderService.controller;

import org.springframework.web.bind.annotation.RestController;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ecom.OrderService.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.ecom.OrderService.model.OrderRequest;
import com.ecom.OrderService.model.OrderResponse;


@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(orderService.placeOrder(orderRequest), HttpStatus.OK);
    }

    @GetMapping("/getOrderDetails/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable long orderId) {
        return new ResponseEntity<>(orderService.getOrderDetails(orderId), HttpStatus.OK);
    }
    
}
