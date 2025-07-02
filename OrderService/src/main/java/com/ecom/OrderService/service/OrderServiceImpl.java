package com.ecom.OrderService.service;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecom.OrderService.repository.OrderServiceRepository;

import lombok.extern.log4j.Log4j2;

import com.ecom.OrderService.entity.Order;
import com.ecom.OrderService.exception.CustomException;
import com.ecom.OrderService.external.client.PaymentService;
import com.ecom.OrderService.external.client.ProductService;
import com.ecom.OrderService.external.request.PaymentRequest;
import com.ecom.OrderService.external.response.PaymentResponse;
import com.ecom.OrderService.external.response.ProductResponse;
import com.ecom.OrderService.model.OrderRequest;
import com.ecom.OrderService.model.OrderResponse;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    private OrderServiceRepository orderServiceRepository;
    private ProductService productService;
    private PaymentService paymentService;
    private RestTemplate restTemplate;

    public OrderServiceImpl(OrderServiceRepository orderServiceRepository, ProductService productService, PaymentService paymentService, RestTemplate restTemplate) {
        this.orderServiceRepository = orderServiceRepository;
        this.productService = productService;
        this.paymentService = paymentService;
        this.restTemplate = restTemplate;
    }

    public long placeOrder(OrderRequest orderRequest) {
        log.info("Placing order for product id: {}", orderRequest);

        ResponseEntity<Long> productPriceResponse = productService.getProductPrice(orderRequest.getProductId());
        Long productPrice = productPriceResponse.getBody();
        
        if (productPrice == null) {
            log.error("Product not found for id: {}", orderRequest.getProductId());
            throw new RuntimeException("Product not found");
        }

        long payableAmount = productPrice * orderRequest.getQuantity();

        log.info("Payable price for product id: {} is: {}", orderRequest.getProductId(), payableAmount);

        if (payableAmount != orderRequest.getTotalAmount()) {
            log.error("Invalid total amount for product id: {}", orderRequest.getProductId());
            throw new CustomException("Invalid total amount", "INVALID_TOTAL_AMOUNT", 400);
        } else {

            log.info("Payable amount for product id: {} is: {}", orderRequest.getProductId(), payableAmount);

            productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

            log.info("Quantity reduced successfully for product id: {}", orderRequest.getProductId());

            Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .orderDate(Instant.now())
                .amount(orderRequest.getTotalAmount())
                .status("CREATED")
                .build();

            order = orderServiceRepository.save(order);

            log.info("Calling Payment Service for order id: {}", order.getId());

            PaymentRequest paymentRequest = PaymentRequest.builder()
                .amount(payableAmount)
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .build();

                String orderStatus = null;

                try {
                    ResponseEntity<Long> paymentResponse = paymentService.doPayment(paymentRequest);
                    log.info("Payment done successfully for order id: {} with payment id: {}", order.getId(), paymentResponse);
                    orderStatus = "PLACED";
                } catch (Exception ex) {
                    log.error("Payment failed for order id: {}", order.getId());
                    orderStatus = "PAYMENT_FAILED";
                    productService.increaseQuantity(orderRequest.getProductId(), orderRequest.getQuantity());
                }
                
                order.setStatus(orderStatus);
                orderServiceRepository.save(order);

            log.info("Order placed successfully for product id: {}", orderRequest.getProductId());

            return order.getId();
        }
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        Order order = orderServiceRepository.findById(orderId).orElseThrow(() -> new CustomException("Order Id not valid", "INVALID ORDER ID", 400));
        OrderResponse orderResponse = OrderResponse.builder()
            .orderId(order.getId())
            .orderDate(order.getOrderDate())
            .orderStatus(order.getStatus())
            .amount(order.getAmount())
            .build();

        ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductResponse.class);
        PaymentResponse paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/" + order.getId(), PaymentResponse.class);

        // ProductResponse productResponse = productService.getProduct(order.getProductId()).getBody();
        // PaymentResponse paymentResponse = paymentService.getPaymentDetailByOrderId(orderId).getBody();
        orderResponse.setProduct(productResponse);
        orderResponse.setPayment(paymentResponse);

        return orderResponse;
    }
    
}
