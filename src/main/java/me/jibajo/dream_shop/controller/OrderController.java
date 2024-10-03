package me.jibajo.dream_shop.controller;

import me.jibajo.dream_shop.dto.OrderDTO;
import me.jibajo.dream_shop.exception.ProductNotFoundException;
import me.jibajo.dream_shop.model.Order;
import me.jibajo.dream_shop.response.APIResponse;
import me.jibajo.dream_shop.service.order.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api-prefix}/orders")
public class OrderController {
    @Autowired
    private IOrderService iOrderService;

    @PostMapping("/order")
    public ResponseEntity<APIResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order = iOrderService.placeOrder(userId);
            OrderDTO orderDTO = iOrderService.converToDTO(order);
            return ResponseEntity.ok(new APIResponse("Ordered successfully", orderDTO));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse("Something went wrong", e.getMessage()));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<APIResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDTO orderDTO = iOrderService.getOrder(orderId);
            return ResponseEntity.ok(new APIResponse("Success", orderDTO));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{userId}/order")
    public ResponseEntity<APIResponse> getUserOrder(@PathVariable Long userId) {
        try {
            List<OrderDTO> orderList = iOrderService.getUserOrder(userId);
            return ResponseEntity.ok(new APIResponse("Success", orderList));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }
}
