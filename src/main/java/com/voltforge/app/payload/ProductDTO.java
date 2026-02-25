package com.voltforge.app.payload;

public class ProductDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    private double productSpecialPrice;
    private double productDiscountPercentage;
    private Integer productQuantity;
    private String productImage;
    private CategoryDTO productCategory;

    public ProductDTO() {
    }

    public ProductDTO(Long productId,
                      String productName,
                      String productDescription,
                      double productPrice,
                      double productSpecialPrice,
                      double productDiscountPercentage,
                      Integer productQuantity,
                      String productImage,
                      CategoryDTO productCategory) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productSpecialPrice = productSpecialPrice;
        this.productDiscountPercentage = productDiscountPercentage;
        this.productQuantity = productQuantity;
        this.productImage = productImage;
        this.productCategory = productCategory;
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

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductSpecialPrice() {
        return productSpecialPrice;
    }

    public void setProductSpecialPrice(double productSpecialPrice) {
        this.productSpecialPrice = productSpecialPrice;
    }

    public double getProductDiscountPercentage() {
        return productDiscountPercentage;
    }

    public void setProductDiscountPercentage(double productDiscountPercentage) {
        this.productDiscountPercentage = productDiscountPercentage;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public CategoryDTO getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(CategoryDTO productCategory) {
        this.productCategory = productCategory;
    }
}
