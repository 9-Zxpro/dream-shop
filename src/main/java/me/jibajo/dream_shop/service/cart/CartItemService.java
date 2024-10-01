package me.jibajo.dream_shop.service.cart;

import me.jibajo.dream_shop.exception.ResourceNotFoundException;
import me.jibajo.dream_shop.model.Cart;
import me.jibajo.dream_shop.model.CartItem;
import me.jibajo.dream_shop.model.Product;
import me.jibajo.dream_shop.repository.CartItemRepository;
import me.jibajo.dream_shop.repository.CartRepository;
import me.jibajo.dream_shop.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartItemService implements ICartItemService{
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private ICartService iCartService;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        Cart cart = iCartService.getCart(cartId);
        Product product = iProductService.getProductById(productId);
        CartItem cartItem = cart.getCartItemSet().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(new CartItem());
        if(cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemToCart(Long cartId, Long productId) {
        Cart cart = iCartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, productId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = iCartService.getCart(cartId);
        cart.getCartItemSet().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getCartItemSet().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = iCartService.getCart(cartId);
        return cart.getCartItemSet().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found."));
    }
}
