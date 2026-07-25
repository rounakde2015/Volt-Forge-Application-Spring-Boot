package com.voltforge.app.service;

import com.voltforge.app.exception.APIException;
import com.voltforge.app.exception.ResourceNotFoundException;
import com.voltforge.app.model.Cart;
import com.voltforge.app.model.CartItem;
import com.voltforge.app.model.Product;
import com.voltforge.app.payload.CartDTO;
import com.voltforge.app.payload.ProductDTO;
import com.voltforge.app.repository.CartItemRepository;
import com.voltforge.app.repository.CartRepository;
import com.voltforge.app.repository.ProductRepository;
import com.voltforge.app.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, Integer productQuantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (cartItem != null) {
            throw new APIException("Product" + product.getProductName() + " already exists");
        }

        if (product.getProductQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getProductQuantity() < productQuantity) {
            throw new APIException("Please make an order of the " +  product.getProductName() + " less than or equal to " + product.getProductQuantity());
        }

        CartItem cartItemToAdd = new CartItem();

        cartItemToAdd.setProduct(product);
        cartItemToAdd.setCart(cart);
        cartItemToAdd.setQuantity(productQuantity);
        cartItemToAdd.setDiscount(product.getProductDiscountPercentage());
        cartItemToAdd.setProductPrice(product.getProductSpecialPrice());

        cartItemRepository.save(cartItemToAdd);

        cart.getCartItems().add(cartItemToAdd);

        product.setProductQuantity(product.getProductQuantity());

        cart.setTotalPrice(cart.getTotalPrice() + (product.getProductPrice() *productQuantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream()
                .map(item -> {
                    ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                    productDTO.setProductQuantity(item.getQuantity());
                    return productDTO;
                });

        cartDTO.setProducts(productDTOStream.toList());


        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.isEmpty()) {
            throw new APIException("No carts found");
        }

        return carts.stream().
                map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    List<ProductDTO> productDTOs = cart.getCartItems().stream()
                            .map(product -> modelMapper.map(product.getProduct(), ProductDTO.class))
                            .collect(Collectors.toList());
                    cartDTO.setProducts(productDTOs);
                    return cartDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public CartDTO getCartByUserEmailId(String userEmailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(userEmailId, cartId);

        if (cart  == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        cart.getCartItems().forEach(cartItem -> cartItem.getProduct().setProductQuantity(cartItem.getQuantity()));

        List<ProductDTO> products = cart.getCartItems().stream()
                .map(item -> modelMapper.map(item.getProduct(), ProductDTO.class))
                .toList();

        cartDTO.setProducts(products);

        return cartDTO;
    }

    @Override
    public CartDTO updateProductQuantityInCart(Long productId, int productQuantity) {
        String userEmail = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByUserEmail(userEmail);
        Long userCartId = userCart.getCartId();

        Cart cart = cartRepository.findById(userCartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "userCartId", userCartId));

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getProductQuantity() == 0) {
            throw new APIException(product.getProductName() + " is not available");
        }

        if (product.getProductQuantity() < product.getProductQuantity()) {
            throw new APIException("Please make an order of the " + product.getProductName() + " less than or equal to " + product.getProductQuantity());
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(userCartId, productId);

        if (cartItem == null) {
            throw new APIException("Product" + product.getProductName() + " does not exist");
        }

        cartItem.setProductPrice(product.getProductPrice());
        cartItem.setQuantity(cartItem.getQuantity() + productQuantity);
        cartItem.setDiscount(product.getProductDiscountPercentage());
        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * productQuantity));

        cartRepository.save(cart);

        CartItem updatedCartItems = cartItemRepository.save(cartItem);

        if(updatedCartItems.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedCartItems.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(updatedCartItems, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setProductQuantity(item.getQuantity());
            return productDTO;
        });

        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;
    }

    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);

        if(cartItem == null) {
          throw new ResourceNotFoundException("Product", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return "Product" + cartItem.getProduct().getProductName() + " has been deleted";
    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByUserEmail((authUtil.loggedInEmail()));

        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());

        return cartRepository.save(cart);

    }
}
