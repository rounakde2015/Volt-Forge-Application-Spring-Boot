package com.voltforge.app.service;

import com.voltforge.app.model.CategoryModel;

import java.util.List;

public interface CategoryService {
    List<CategoryModel> getAllCategories();

    void addCategory(CategoryModel category);

    String deleteCategory(Long categoryId);

    String updateCategory(Long categoryId,  CategoryModel category);
}
