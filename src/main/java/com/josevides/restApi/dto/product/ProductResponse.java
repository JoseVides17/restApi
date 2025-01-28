package com.josevides.restApi.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponse {

    private String name;
    private String description;
    private Float price;
    private Integer quantity;
    private String status;
    private String message;

}
