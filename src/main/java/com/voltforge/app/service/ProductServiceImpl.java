package com.voltforge.app.service;

import com.voltforge.app.exception.APIException;
import com.voltforge.app.exception.ResourceNotFoundException;
import com.voltforge.app.model.CategoryModel;
import com.voltforge.app.model.ProductModel;
import com.voltforge.app.payload.ProductDTO;
import com.voltforge.app.payload.ProductResponse;
import com.voltforge.app.repository.CategoryRepository;
import com.voltforge.app.repository.ProductRepository;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        CategoryModel category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        AtomicBoolean isProductPresent = new AtomicBoolean(true);

        List<ProductModel> products = category.getProducts();

        for (ProductModel product : products) {
            if (product.getProductName().equals(productDTO.getProductName())) {
                isProductPresent.set(false);
                break;
            }
        }

        if(isProductPresent.get()) {
            ProductModel productModel = modelMapper.map(productDTO, ProductModel.class);

            double specialPrice = productModel.getProductPrice() - ((productModel.getProductDiscountPercentage() * 0.01) * productModel.getProductPrice());

            productModel.setCategoryModel(category);
            productModel.setProductImage("default.png");
            productModel.setProductSpecialPrice(specialPrice);

            ProductModel savedProduct = productRepository.save(productModel);

            return modelMapper.map(savedProduct, ProductDTO.class);

        } else {
            throw new APIException("Product already exists !!!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortProductOrderAndBy = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortProductOrderAndBy);
        Page<ProductModel> productPage = productRepository.findAll(pageDetails);

        List<ProductModel> allProducts = productPage.getContent();

        if (allProducts.isEmpty()) {
            throw new APIException("No Products created !!!");
        }

        List<ProductDTO> productDTOS = allProducts.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getNumberOfElements());
        productResponse.setLastPage(productPage.isLast());

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
    public ProductResponse getProductByKeyWord(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortProductOrderAndBy = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortProductOrderAndBy);
        Page<ProductModel> productPage = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<ProductModel> allProducts = productPage.getContent();

        if  (allProducts.isEmpty()) {
            throw new APIException("No Products found with the keyword " + keyword);
        }

        List<ProductDTO> productDTOS = allProducts.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getNumberOfElements());
        productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productID, ProductDTO productDTO) {
        ProductModel productFromDB = productRepository.findById(productID)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productID));

        ProductModel productModel = modelMapper.map(productDTO, ProductModel.class);

        productFromDB.setProductName(productModel.getProductName());
        productFromDB.setProductDescription(productModel.getProductDescription());
        productFromDB.setProductPrice(productModel.getProductPrice());
        productFromDB.setProductDiscountPercentage(productModel.getProductDiscountPercentage());
        productFromDB.setProductSpecialPrice(productModel.getProductPrice() - ((productModel.getProductDiscountPercentage() * 0.01) * productModel.getProductPrice()));

        ProductModel savedProduct = productRepository.save(productFromDB);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productID, MultipartFile productImageFile) throws IOException {
        ProductModel productFromDB = productRepository.findById(productID)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productID));

        String productImageFileName = fileService.uploadImageInServer(path, productImageFile);

        productFromDB.setProductImage(productImageFileName);
        ProductModel updatedProduct = productRepository.save(productFromDB);

        return modelMapper.map(updatedProduct, ProductDTO.class);
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
