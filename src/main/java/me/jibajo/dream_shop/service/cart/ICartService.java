package me.jibajo.dream_shop.service.cart;

import me.jibajo.dream_shop.model.Cart;
import me.jibajo.dream_shop.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeCartId(User user);

    Cart getCartByUserId(Long userId);
}
