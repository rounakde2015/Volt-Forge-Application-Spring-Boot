package com.voltforge.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity(name = "VoltForgeCategories")

public class CategoryModel {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;

    @NotBlank
    @Size(min = 5, message = "Category name must contain atleast 5 characters.")
    private String categoryName;

    @OneToMany(mappedBy = "categoryModel",  cascade = CascadeType.ALL)
    private List<ProductModel> products;

    public CategoryModel(Long categoryId, String categoryName,  List<ProductModel> products) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.products = products;
    }

    public CategoryModel() {

    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}
