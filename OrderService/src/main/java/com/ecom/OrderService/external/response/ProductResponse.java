package com.ecom.OrderService.external.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private long id;
    private String name;
    private long price;
    private long quantity;
    private String description;
}
