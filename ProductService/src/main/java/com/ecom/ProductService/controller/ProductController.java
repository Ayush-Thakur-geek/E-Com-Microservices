package com.ecom.ProductService.controller;

import com.ecom.ProductService.model.ProductRequest;
import com.ecom.ProductService.model.ProductResponse;
import com.ecom.ProductService.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.addProduct(productRequest), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getProduct(id), HttpStatus.OK);
    }

    @PutMapping("reduceQuantity/{id}")
    public ResponseEntity<Void> reduceQuantity(@PathVariable("id") Long id, @RequestParam Long quantity) {
        productService.reduceQuantity(id, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("increaseQuantity/{id}")
    public ResponseEntity<Void> increaseQuantity(@PathVariable("id") Long id, @RequestParam Long quantity) {
        productService.increaseQuantity(id, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("price/{id}")
    public ResponseEntity<Long> getProductPrice(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getProductPrice(id), HttpStatus.OK);
    }
}
