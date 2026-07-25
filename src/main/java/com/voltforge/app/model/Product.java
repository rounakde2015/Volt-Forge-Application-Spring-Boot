package com.voltforge.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "VoltForgeProducts")
public class Product {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product name must contain atleast 3 characters")
    private String productName;

    @NotBlank
    @Size(min = 6, message = "Product name must contain atleast 6 characters")
    private String productDescription;

    private Integer productQuantity;

    private double productPrice;

    private double productDiscountPercentage;

    private double productSpecialPrice;

    private String productImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<CartItem> products = new ArrayList<>();

    public Product() {
    }

    public Product(Long productId,
                   String productName,
                   String productDescription,
                   double productPrice,
                   Integer productQuantity,
                   double productDiscountPercentage,
                   double productSpecialPrice,
                   String productImage,
                   Category category,
                   User user) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productDiscountPercentage = productDiscountPercentage;
        this.productSpecialPrice = productSpecialPrice;
        this.productImage = productImage;
        this.category = category;
        this.user = user;
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

    public Category getCategoryModel() {
        return category;
    }

    public void setCategoryModel(Category category) {
        this.category = category;
    }

    public User getUserModel() {
        return user;
    }

    public void setUserModel(User user) {
        this.user = user;
    }
}
