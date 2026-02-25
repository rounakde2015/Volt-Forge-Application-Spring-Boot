package com.voltforge.app.service;

import com.voltforge.app.model.ProductModel;
import com.voltforge.app.payload.ProductDTO;
import com.voltforge.app.payload.ProductResponse;
import jakarta.validation.Valid;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductModel productModel);

    ProductResponse getAllProducts();

    ProductResponse getProductById(Long productId);

    ProductResponse getProductByCategoryId(Long categoryId);

    ProductResponse getProductByKeyWord(String keyword);

    ProductDTO updateProduct(Long productID, ProductModel productModel);

    ProductDTO deleteProduct(Long productID);
}
