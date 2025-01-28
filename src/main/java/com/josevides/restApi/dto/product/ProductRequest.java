package com.josevides.restApi.dto.product;

import lombok.Data;

@Data
public class ProductRequest {

    private String name;
    private String description;
    private Float price;
    private Integer quantity;
    private String status;

}
