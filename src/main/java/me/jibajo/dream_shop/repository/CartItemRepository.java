package me.jibajo.dream_shop.repository;

import me.jibajo.dream_shop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllById(Long id);
}
