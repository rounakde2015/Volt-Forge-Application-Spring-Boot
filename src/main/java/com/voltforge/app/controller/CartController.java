package com.voltforge.app.controller;

import com.voltforge.app.model.Cart;
import com.voltforge.app.payload.CartDTO;
import com.voltforge.app.repository.CartRepository;
import com.voltforge.app.service.CartService;
import com.voltforge.app.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CartController {
    @Autowired
    CartService cartService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AuthUtil authUtil;

    @PostMapping("/carts/products/{productId}/quantity/{productQuantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, @PathVariable Integer productQuantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId, productQuantity);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    @Operation(summary = "Get all Carts", description = "Get the list of all carts created by users")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> cartDTOS = cartService.getAllCarts();

        return new ResponseEntity<List<CartDTO>>(cartDTOS, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartByUserEmailId() {
        String userEmailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByUserEmail(userEmailId);
        Long cartId = cart.getCartId();

        CartDTO cartDTO = cartService.getCartByUserEmailId(userEmailId, cartId);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @PutMapping("/carts/product/{productId}/quantity/{cartOperation}")
    public ResponseEntity<CartDTO> updateCartProductQuantity(@PathVariable Long productId, @PathVariable String cartOperation) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, cartOperation.equalsIgnoreCase("delete") ? -1 : 1);

        System.out.println("Cart DTO = " + cartDTO);

        return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/cart/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);

    }
}
