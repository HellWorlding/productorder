package com.productorder.order.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {

    @NotBlank
    private String name;

    @Min(0)
    private int price;

    @Min(0)
    private int stock;
}