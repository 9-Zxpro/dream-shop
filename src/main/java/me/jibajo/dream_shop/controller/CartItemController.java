package me.jibajo.dream_shop.controller;

import io.jsonwebtoken.JwtException;
import me.jibajo.dream_shop.exception.ResourceNotFoundException;
import me.jibajo.dream_shop.model.Cart;
import me.jibajo.dream_shop.model.User;
import me.jibajo.dream_shop.response.APIResponse;
import me.jibajo.dream_shop.service.cart.ICartItemService;
import me.jibajo.dream_shop.service.cart.ICartService;
import me.jibajo.dream_shop.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("${api-prefix}/cartItems")
public class CartItemController {
    @Autowired
    private ICartItemService iCartItemService;
    @Autowired
    private ICartService iCartService;
    @Autowired
    private IUserService iUserService;

    @PostMapping("/item/add")
    public ResponseEntity<APIResponse> addItemToCart(@RequestParam Long productId, @RequestParam Integer quantity){
        try {
            User user = iUserService.getAuthenticatedUser();
            Cart cart = iCartService.initializeCartId(user);
            iCartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new APIResponse("Item added successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage() , null));
        } catch (JwtException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new APIResponse(e.getMessage() , null));
        }
    }

    @DeleteMapping("/item/delete/cartId={cartId}/productId={productId}")
    public ResponseEntity<APIResponse> removeItemToCart(@PathVariable Long cartId, @PathVariable Long productId) {
        try {
            iCartItemService.removeItemToCart(cartId, productId);
            return ResponseEntity.ok(new APIResponse("Item removed successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage() , null));
        }
    }

    @PutMapping("/item/update/cartId={cartId}/productId={productId}")
    public ResponseEntity<APIResponse> updateItemQuantity(@PathVariable Long cartId, @PathVariable Long productId, @RequestParam int quantity){
        try {
            iCartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new APIResponse("Quantity updated successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage() , null));
        }
    }

//    @GetMapping("/item/cartId={cartId}/productId={productId}")
//    public ResponseEntity<APIResponse> getCartItem(@PathVariable Long cartId, @PathVariable Long productId){
//        try {
//            CartItem cartItem = iCartItemService.getCartItem(cartId, productId);
//            return ResponseEntity.ok(new APIResponse("Success", cartItem));
//        } catch (Exception e) {
//            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage() , null));
//        }
//    }

}
