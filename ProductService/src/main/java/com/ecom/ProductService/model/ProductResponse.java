package com.ecom.ProductService.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private Long price;
    private Long quantity;
    private String description;
}
