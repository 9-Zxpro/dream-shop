package me.jibajo.dream_shop.dto;

import lombok.Data;
import me.jibajo.dream_shop.model.Category;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String brand;
    private String description;
    private int inventory;
    private Category category;
    private List<ImageDTO> imageList;
}
