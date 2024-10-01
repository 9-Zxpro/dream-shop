package me.jibajo.dream_shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<CartItem> cartItemSet = new HashSet<>();

    public void addItem(CartItem cartItem) {
        this.cartItemSet.add(cartItem);
        cartItem.setCart(this);
        updateTotalAmount();
    }
    public void removeItem(CartItem cartItem) {
        this.cartItemSet.remove(cartItem);
        cartItem.setCart(null);
        updateTotalAmount();
    }
    private void updateTotalAmount() {
        this.totalAmount = this.cartItemSet.stream().map(item -> {
            BigDecimal unitPrice = item.getUnitPrice();
            if (unitPrice == null) {
                return BigDecimal.ZERO;
            }
            return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
