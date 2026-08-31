package com.voltforge.app.repository;

import com.voltforge.app.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM VoltForgeCarts c WHERE c.user.userEmail = ?1")
    Cart findCartByUserEmail(String userEmail);

    @Query("SELECT c FROM VoltForgeCarts c WHERE c.user.userEmail = ?1 and c.cartId = ?2")
    Cart findCartByEmailAndCartId(String userEmailId, Long cartId);

    @Query("SELECT c FROM VoltForgeCarts c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.productId = ?1")
    List<Cart> findCartsByProductId(Long productID);
}
