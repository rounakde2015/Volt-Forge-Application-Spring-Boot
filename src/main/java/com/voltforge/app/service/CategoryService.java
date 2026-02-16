package com.voltforge.app.service;

import com.voltforge.app.model.CategoryModel;
import com.voltforge.app.payload.CategoryDTO;
import com.voltforge.app.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO addCategory(CategoryDTO categoryDto);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(Long categoryId,  CategoryDTO categoryDTO);
}
