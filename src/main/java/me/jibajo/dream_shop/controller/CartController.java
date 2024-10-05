package me.jibajo.dream_shop.controller;

import me.jibajo.dream_shop.exception.ResourceNotFoundException;
import me.jibajo.dream_shop.model.Cart;
import me.jibajo.dream_shop.response.APIResponse;
import me.jibajo.dream_shop.service.cart.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api-prefix}/cart")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getCart(@PathVariable Long id) {
        try {
            Cart cart = iCartService.getCart(id);
            return ResponseEntity.ok(new APIResponse("Success", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/clear/{id}")
    public ResponseEntity<APIResponse> clearCart(@PathVariable Long id) {
        try {
            iCartService.clearCart(id);
            return ResponseEntity.ok(new APIResponse("Deleted Successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }

    }

    @GetMapping("/total-price/{id}")
    public ResponseEntity<APIResponse> getTotalPrice(@PathVariable Long id) {
        try {
            BigDecimal totalPrice = iCartService.getTotalPrice(id);
            return ResponseEntity.ok(new APIResponse("Total price ", totalPrice));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage(), null));
        }

    }


}
