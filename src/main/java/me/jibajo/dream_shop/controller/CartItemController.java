package me.jibajo.dream_shop.controller;

import me.jibajo.dream_shop.exception.ResourceNotFoundException;
import me.jibajo.dream_shop.model.CartItem;
import me.jibajo.dream_shop.response.APIResponse;
import me.jibajo.dream_shop.service.cart.ICartItemService;
import me.jibajo.dream_shop.service.cart.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api-prefix}/cartItems")
public class CartItemController {
    @Autowired
    private ICartItemService iCartItemService;
    @Autowired
    private ICartService iCartService;

    @PostMapping("/item/add")
    public ResponseEntity<APIResponse> addItemToCart(@RequestParam(required = false) Long cartId, @RequestParam Long productId, @RequestParam Integer quantity){
        try {
            if(cartId == null) {
                cartId = iCartService.initializeCartId();
            }
            iCartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new APIResponse("Item added successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(e.getMessage() , null));
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
