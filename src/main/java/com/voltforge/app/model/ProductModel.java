package com.voltforge.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity(name = "VoltForgeProducts")
public class ProductModel {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    @Size(max = 3, message = "Product name must contain atleast 3 characters")
    private String productName;

    @NotBlank
    @Size(max = 6, message = "Product name must contain atleast 6 characters")
    private String productDescription;

    private Integer productQuantity;

    private double productPrice;

    private double productDiscountPercentage;

    private double productSpecialPrice;

    private String productImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryModel categoryModel;

    public ProductModel() {
    }

    public ProductModel(Long productId,
                        String productName,
                        String productDescription,
                        double productPrice,
                        Integer productQuantity,
                        double productDiscountPercentage,
                        double productSpecialPrice,
                        String productImage,
                        CategoryModel categoryModel) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productDiscountPercentage = productDiscountPercentage;
        this.productSpecialPrice = productSpecialPrice;
        this.productImage = productImage;
        this.categoryModel = categoryModel;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductDiscountPercentage() {
        return productDiscountPercentage;
    }

    public void setProductDiscountPercentage(double productDiscountPercentage) {
        this.productDiscountPercentage = productDiscountPercentage;
    }

    public double getProductSpecialPrice() {
        return productSpecialPrice;
    }

    public void setProductSpecialPrice(double productSpecialPrice) {
        this.productSpecialPrice = productSpecialPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }
}
