package com.ecom.ProductService.model;

import lombok.Data;

@Data
public class ProductRequest {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Long quantity;
}
