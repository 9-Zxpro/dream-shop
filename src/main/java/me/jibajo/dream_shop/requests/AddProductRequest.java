package me.jibajo.dream_shop.requests;

import lombok.Data;
import me.jibajo.dream_shop.model.Category;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private String brand;
    private String description;
    private int inventory;
    private Category category;
}
