package com.voltforge.app.service;


import com.voltforge.app.payload.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {

    CartDTO addProductToCart(Long productId, Integer productQuantity);

    List<CartDTO> getAllCarts();

    CartDTO getCartByUserEmailId(String userEmailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, int delete);

    String deleteProductFromCart(Long cartId, Long productId);
}
