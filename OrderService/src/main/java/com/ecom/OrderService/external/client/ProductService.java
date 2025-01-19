package com.ecom.OrderService.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecom.OrderService.exception.CustomException;
import com.ecom.OrderService.external.response.ProductResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name="PRODUCT-SERVICE/product")
public interface ProductService {

    @PutMapping("reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable("id") Long id, @RequestParam Long quantity);

    @PutMapping("increaseQuantity/{id}")
    public ResponseEntity<Void> increaseQuantity(@PathVariable("id") Long id, @RequestParam Long quantity);

    @GetMapping("price/{id}")
    public ResponseEntity<Long> getProductPrice(@PathVariable Long id);

    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id);

    default void fallback(Exception e) {
        throw new CustomException("Product Service is down", "UNAVAILABLE", 500);
    }

}