package com.voltforge.app.service;

import com.voltforge.app.exception.APIException;
import com.voltforge.app.exception.ResourceNotFoundException;
import com.voltforge.app.model.Category;
import com.voltforge.app.model.Product;
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
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        AtomicBoolean isProductPresent = new AtomicBoolean(true);

        List<Product> products = category.getProducts();

        for (Product product : products) {
            if (product.getProductName().equals(productDTO.getProductName())) {
                isProductPresent.set(false);
                break;
            }
        }

        if(isProductPresent.get()) {
            Product product = modelMapper.map(productDTO, Product.class);

            double specialPrice = product.getProductPrice() - ((product.getProductDiscountPercentage() * 0.01) * product.getProductPrice());

            product.setCategoryModel(category);
            product.setProductImage("default.png");
            product.setProductSpecialPrice(specialPrice);

            Product savedProduct = productRepository.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);

        } else {
            throw new APIException("Product already exists !!!");
        }
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortProductOrderAndBy = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortProductOrderAndBy);
        Page<Product> productPage = productRepository.findAll(pageDetails);

        List<Product> allProducts = productPage.getContent();

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
        List<Product> product = productRepository.findById(productId).stream().toList();

        List<ProductDTO> productById = product.stream()
                .map(productModel -> modelMapper.map(productModel, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productById);

        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<Product> product = productRepository.findByCategoryModel(category);

        return getProductResponse(product);
    }

    @Override
    public ProductResponse getProductByKeyWord(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortProductOrderAndBy = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortProductOrderAndBy);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%', pageDetails);

        List<Product> allProducts = productPage.getContent();

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
        Product productFromDB = productRepository.findById(productID)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productID));

        Product product = modelMapper.map(productDTO, Product.class);

        productFromDB.setProductName(product.getProductName());
        productFromDB.setProductDescription(product.getProductDescription());
        productFromDB.setProductPrice(product.getProductPrice());
        productFromDB.setProductDiscountPercentage(product.getProductDiscountPercentage());
        productFromDB.setProductSpecialPrice(product.getProductPrice() - ((product.getProductDiscountPercentage() * 0.01) * product.getProductPrice()));

        Product savedProduct = productRepository.save(productFromDB);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productID, MultipartFile productImageFile) throws IOException {
        Product productFromDB = productRepository.findById(productID)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productID));

        String productImageFileName = fileService.uploadImageInServer(path, productImageFile);

        productFromDB.setProductImage(productImageFileName);
        Product updatedProduct = productRepository.save(productFromDB);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productID) {
        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productID));

        productRepository.delete(product);

        return modelMapper.map(product, ProductDTO.class);
    }

    @NonNull
    private ProductResponse getProductResponse(List<Product> product) {
        if (product.isEmpty()) {
            throw new APIException("No products found");
        }

        List<ProductDTO> productDTOs = product.stream()
                .map(productModel -> modelMapper.map(productModel, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setProducts(productDTOs);

        return productResponse;
    }
}
