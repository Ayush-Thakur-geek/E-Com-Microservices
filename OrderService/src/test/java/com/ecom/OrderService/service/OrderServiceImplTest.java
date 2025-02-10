package com.ecom.OrderService.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ecom.OrderService.entity.Order;
import com.ecom.OrderService.exception.CustomException;
import com.ecom.OrderService.external.client.PaymentService;
import com.ecom.OrderService.external.client.ProductService;
import com.ecom.OrderService.external.request.PaymentRequest;
import com.ecom.OrderService.external.response.PaymentResponse;
import com.ecom.OrderService.external.response.ProductResponse;
import com.ecom.OrderService.model.OrderRequest;
import com.ecom.OrderService.model.OrderResponse;
import com.ecom.OrderService.model.PaymentMode;
import com.ecom.OrderService.repository.OrderServiceRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderServiceRepository orderServiceRepository;

    @Mock
    private ProductService productService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderServiceRepository, productService, paymentService, restTemplate);
    }

    @DisplayName("Get Order - Success Scenario")
    @Test
    void test_When_Order_Success() {
        Order order = getMockOrder();
        Mockito.when(orderServiceRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(order));

        Mockito.when(restTemplate.getForObject(
            "http://PRODUCT-SERVICE/product/" + order.getProductId(), 
            ProductResponse.class)
            ).thenReturn(getMockProductResponse());

        Mockito.when(restTemplate.getForObject(
            "http://PAYMENT-SERVICE/payment/" + order.getId(), 
            PaymentResponse.class)
            ).thenReturn(getMockPaymentResponse());
        
        OrderResponse orderResponse = orderService.getOrderDetails(5);

        verify(orderServiceRepository, times(1)).findById(ArgumentMatchers.anyLong());
        verify(restTemplate, times(1)).getForObject(
            "http://PRODUCT-SERVICE/product/" + order.getProductId(), 
            ProductResponse.class
            );
        verify(restTemplate, times(1)).getForObject(
            "http://PAYMENT-SERVICE/payment/" + order.getId(), 
            PaymentResponse.class
            );

        assertNotNull(orderResponse);
        assertEquals(order.getId(), orderResponse.getOrderId());
    }

    @DisplayName("Get Order - Failure Scenario")
    @Test
    void test_When_Order_NOT_FOUND_then_Not_Found() {
        Mockito.when(orderServiceRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        CustomException exception = 
                Assertions.assertThrows(CustomException.class, 
                        () -> orderService.getOrderDetails(5));

        assertEquals("INVALID ORDER ID", exception.getErrorCode());
        assertEquals(400, exception.getStatus());

        verify(orderServiceRepository, times(1))
                .findById(ArgumentMatchers.anyLong());
    }

    @DisplayName("Place Order - Failure Scenario")
    @Test
    void test_when_Place_Order_Payment_Fails_then_Order_Placed() {
        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();

        Mockito.when(orderServiceRepository.save(any(Order.class))).thenReturn(order);

        Mockito.when(productService.reduceQuantity(anyLong(), anyLong()))
            .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));

        Mockito.when(paymentService.doPayment(ArgumentMatchers.any(PaymentRequest.class)))
            .thenThrow(new RuntimeException());

        Mockito.when(productService.getProductPrice(anyLong()))
            .thenReturn(new ResponseEntity<>(100L, HttpStatus.OK));

        long orderId = orderService.placeOrder(orderRequest);

        verify(orderServiceRepository, times(2)).save(any());
        verify(productService, times(1)).reduceQuantity(anyLong(), anyLong());
        verify(paymentService, times(1)).doPayment(any(PaymentRequest.class));
        verify(productService, times(1)).getProductPrice(anyLong());

        assertEquals(order.getId(), orderId);
    }

    private Order getMockOrder() {
        return Order.builder()
                .id(5)
                .productId(1)
                .quantity(2)
                .amount(200)
                .orderDate(Instant.now())
                .status("PLACED")
                .build();
    }

    private ProductResponse getMockProductResponse() {
        return ProductResponse.builder()
                .id(1)
                .name("Product 1")
                .price(100)
                .build();
    }

    private PaymentResponse getMockPaymentResponse() {
        return PaymentResponse.builder()
                .paymentId(4)
                .paymentMode(PaymentMode.CASH)
                .orderId(9)
                .status("SUCCESS")
                .build();
    }

    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .productId(1)
                .quantity(2)
                .totalAmount(200)
                .build();
    }
}