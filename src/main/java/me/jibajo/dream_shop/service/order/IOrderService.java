package me.jibajo.dream_shop.service.order;

import me.jibajo.dream_shop.dto.OrderDTO;
import me.jibajo.dream_shop.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDTO getOrder(Long orderId);

    List<OrderDTO> getUserOrder(Long userId);

    OrderDTO converToDTO(Order order);
}
