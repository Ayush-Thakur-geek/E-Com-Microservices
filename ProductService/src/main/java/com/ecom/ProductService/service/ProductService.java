package com.ecom.ProductService.service;

import com.ecom.ProductService.model.ProductRequest;
import com.ecom.ProductService.model.ProductResponse;
public interface ProductService {
    long addProduct(ProductRequest productResponse);
    ProductResponse getProduct(Long id);
    void reduceQuantity(Long id, Long quantity);
    Long getProductPrice(Long id);
    void increaseQuantity(Long id, Long quantity);
}
