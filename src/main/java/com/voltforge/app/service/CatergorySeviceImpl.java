package com.voltforge.app.service;

import com.voltforge.app.exception.APIException;
import com.voltforge.app.exception.ResourceNotFoundException;
import com.voltforge.app.model.CategoryModel;
import com.voltforge.app.respository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatergorySeviceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryModel> getAllCategories() {
        List<CategoryModel> allCategories = categoryRepository.findAll();

        if (allCategories.isEmpty()) {
            throw new APIException("No Categories created !!!");
        }

        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(CategoryModel category) {
        CategoryModel savedCategory = categoryRepository.findByCategoryName((category.getCategoryName()));
        System.out.println(savedCategory);

        if (savedCategory != null) {
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists");
        }

        categoryRepository.save(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        CategoryModel category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category",  "CategoryId", categoryId));

        categoryRepository.delete(category);

        return "Category with categoryId: " + categoryId + " has been deleted";
    }

    @Override
    public CategoryModel updateCategory(Long categoryId,  CategoryModel category) {
        categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category",  "CategoryId", categoryId));

        category.setCategoryId(categoryId);

        return categoryRepository.save(category);
    }
}
