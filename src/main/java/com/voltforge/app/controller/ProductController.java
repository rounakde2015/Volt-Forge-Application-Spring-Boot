package com.voltforge.app.controller;

import com.voltforge.app.config.AppConstants;
import com.voltforge.app.model.ProductModel;
import com.voltforge.app.payload.ProductDTO;
import com.voltforge.app.payload.ProductResponse;
import com.voltforge.app.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,
                                                 @PathVariable Long categoryId) {
        ProductDTO savedProductDTO = productService.addProduct(categoryId, productDTO);

        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                          @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                          @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                          @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_PRODUCTS_ORDER, required = false) String sortOrder) {

        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    
    @GetMapping("/public/product/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long productId) {
        ProductResponse productResponse = productService.getProductById(productId);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/category/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategoryId(@PathVariable Long categoryId) {
        ProductResponse productResponse = productService.getProductByCategoryId(categoryId);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword,
                                                               @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                               @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                               @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                               @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_PRODUCTS_ORDER, required = false) String sortOrder) {

        ProductResponse productResponse = productService.getProductByKeyWord(keyword, pageNumber, pageSize, sortBy, sortOrder);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/admin/product/{productID}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productID,
                                                    @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProductDTO = productService.updateProduct(productID, productDTO);

        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }

    @PutMapping("/admin/product/{productID}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productID,
                                                         @RequestParam("image") MultipartFile productImageFile) throws IOException {
        ProductDTO updatedProductImage = productService.updateProductImage(productID, productImageFile);

        return new ResponseEntity<>(updatedProductImage, HttpStatus.OK);
    }

    @DeleteMapping("/admin/product/{productID}")
    public  ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productID) {
        ProductDTO productResponse = productService.deleteProduct(productID);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

}
