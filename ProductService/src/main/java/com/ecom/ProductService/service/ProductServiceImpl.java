package com.ecom.ProductService.service;

import com.ecom.ProductService.entity.Product;
import com.ecom.ProductService.exception.ProductServiceCustomException;
import com.ecom.ProductService.model.ProductRequest;
import com.ecom.ProductService.model.ProductResponse;
import com.ecom.ProductService.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;

    ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public long addProduct(ProductRequest productResponse) {
        Product product = Product.builder()
                .productName(productResponse.getName())
                .productDescription(productResponse.getDescription())
                .productPrice(productResponse.getPrice())
                .productQuantity(productResponse.getQuantity())
                .build();
        return productRepository.save(product).getId();
    }

    @Override
    public ProductResponse getProduct(Long id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductServiceCustomException("Product not found", "404:PRODUCT_NOT_FOUND"));
        ProductResponse productResponse = ProductResponse.builder()
            .name(product.getProductName())
            .price(product.getProductPrice())
            .quantity(product.getProductQuantity())
            .description(product.getProductDescription())
            .build();

        return productResponse;
    }

    @Override
    public void reduceQuantity(Long id, Long quantity) {
        log.info("Reducing quantity of product with id: {} by quantity: {}", id, quantity);

        Product product = productRepository.findById(id).orElseThrow(() -> new ProductServiceCustomException("Product not found", "404:PRODUCT_NOT_FOUND"));
        if (product.getProductQuantity() < quantity) {
            throw new ProductServiceCustomException("Not enough quantity available", "400:INSUFFICIENT_QUANTITY");
        } else {
            product.setProductQuantity(product.getProductQuantity() - quantity);
            productRepository.save(product);
            log.info("Quantity reduced successfully");
        }
    }

    @Override
    public Long getProductPrice(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductServiceCustomException("Product not found", "404:PRODUCT_NOT_FOUND"));
        return product.getProductPrice();
    }

    @Override
    public void increaseQuantity(Long id, Long quantity) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductServiceCustomException("Product not found", "404:PRODUCT_NOT_FOUND"));
        product.setProductQuantity(quantity + product.getProductQuantity());
        productRepository.save(product);
    }
}
