package me.jibajo.dream_shop.service.order;

import me.jibajo.dream_shop.dto.OrderDTO;
import me.jibajo.dream_shop.enums.OrderStatus;
import me.jibajo.dream_shop.exception.ResourceNotFoundException;
import me.jibajo.dream_shop.model.*;
import me.jibajo.dream_shop.repository.OrderRepository;
import me.jibajo.dream_shop.repository.ProductRepository;
import me.jibajo.dream_shop.service.cart.ICartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ICartService iCartService;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Order placeOrder(Long userId) {
        Cart cart =  iCartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItem(order, cart);
        order.setOrderItemSet(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order orderSaved = orderRepository.save(order);
        iCartService.clearCart(cart.getId());
        return orderSaved;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItem(Order order, Cart cart) {
        return cart.getCartItemSet().stream().map(new Function<CartItem, OrderItem>() {
            @Override
            public OrderItem apply(CartItem cartItem) {
                Product product = cartItem.getProduct();
                product.setInventory(product.getInventory() - cartItem.getQuantity());
                productRepository.save(product);
                return new OrderItem(
                        order,
                        product,
                        cartItem.getQuantity(),
                        cartItem.getUnitPrice()
                );
            }
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDTO getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::converToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<OrderDTO> getUserOrder(Long userId) {
        List<Order> orderList = orderRepository.findByUserId(userId);
        return orderList.stream().map(this::converToDTO).toList();
    }

    @Override
    public OrderDTO converToDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

}
