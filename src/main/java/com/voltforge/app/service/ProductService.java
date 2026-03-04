package com.voltforge.app.service;

import com.voltforge.app.payload.ProductDTO;
import com.voltforge.app.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductById(Long productId);

    ProductResponse getProductByCategoryId(Long categoryId);

    ProductResponse getProductByKeyWord(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(Long productID, ProductDTO productDTO);

    ProductDTO updateProductImage(Long productID, MultipartFile productImage) throws IOException;

    ProductDTO deleteProduct(Long productID);

}
