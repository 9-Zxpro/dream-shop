package me.jibajo.dream_shop.service.cart;

import me.jibajo.dream_shop.exception.ResourceNotFoundException;
import me.jibajo.dream_shop.model.Cart;
import me.jibajo.dream_shop.repository.CartItemRepository;
import me.jibajo.dream_shop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartService implements ICartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator;
    CartService() {
        cartIdGenerator = new AtomicLong(0);
    }

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart is empty"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllById(id);
        cart.getCartItemSet().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Override
    public Long initializeCartId() {
        Cart cart = new Cart();
        Long newCartId = cartIdGenerator.incrementAndGet();
        cart.setId(newCartId);
        return cartRepository.save(cart).getId();
    }
}
