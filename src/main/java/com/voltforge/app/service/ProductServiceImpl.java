package com.voltforge.app.service;

import com.voltforge.app.exception.APIException;
import com.voltforge.app.exception.ResourceNotFoundException;
import com.voltforge.app.model.CategoryModel;
import com.voltforge.app.model.ProductModel;
import com.voltforge.app.payload.ProductDTO;
import com.voltforge.app.payload.ProductResponse;
import com.voltforge.app.respository.CategoryRepository;
import com.voltforge.app.respository.ProductRepository;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductModel productModel) {
        CategoryModel category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        double specialPrice = productModel.getProductPrice() - ((productModel.getProductDiscountPercentage() * 0.01) * productModel.getProductPrice());

        productModel.setCategoryModel(category);
        productModel.setProductImage("default.png");
        productModel.setProductSpecialPrice(specialPrice);

        ProductModel savedProduct = productRepository.save(productModel);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<ProductModel> productModel = productRepository.findAll();

        List<ProductDTO> allProducts = productModel.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(allProducts);

        return productResponse;
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        List<ProductModel> productModel = productRepository.findById(productId).stream().toList();

        List<ProductDTO> productById = productModel.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productById);

        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategoryId(Long categoryId) {
        CategoryModel categoryModel = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<ProductModel> productModel = productRepository.findByCategoryModel(categoryModel);

        return getProductResponse(productModel);
    }

    @Override
    public ProductResponse getProductByKeyWord(String keyword) {
        List<ProductModel> productModel = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');

        return getProductResponse(productModel);
    }

    @Override
    public ProductDTO updateProduct(Long productID, ProductModel productModel) {
        ProductModel productFromDB = productRepository.findById(productID)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productID));

        productFromDB.setProductName(productModel.getProductName());
        productFromDB.setProductDescription(productModel.getProductDescription());
        productFromDB.setProductPrice(productModel.getProductPrice());
        productFromDB.setProductDiscountPercentage(productModel.getProductDiscountPercentage());
        productFromDB.setProductSpecialPrice(productModel.getProductPrice() - ((productModel.getProductDiscountPercentage() * 0.01) * productModel.getProductPrice()));

        ProductModel savedProduct = productRepository.save(productFromDB);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productID) {
        ProductModel productModel = productRepository.findById(productID)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productID));

        productRepository.delete(productModel);

        return modelMapper.map(productModel, ProductDTO.class);
    }

    @NonNull
    private ProductResponse getProductResponse(List<ProductModel> productModel) {
        if (productModel.isEmpty()) {
            throw new APIException("No products found");
        }

        List<ProductDTO> productDTOs = productModel.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productDTOs);

        return productResponse;
    }




}
