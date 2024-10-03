package me.jibajo.dream_shop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private Long Id;
    private ProductDTO product;
    private int quantity;
    private BigDecimal unitPrice;
}
