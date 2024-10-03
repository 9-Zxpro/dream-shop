package me.jibajo.dream_shop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartDTO {
    private Long cartId;
    private BigDecimal totalAmount;
    private Set<CartItemDTO> cartItemSet;
}
