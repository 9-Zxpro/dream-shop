package me.jibajo.dream_shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDTO> orderDTOList;
    private CartDTO cart;
}
