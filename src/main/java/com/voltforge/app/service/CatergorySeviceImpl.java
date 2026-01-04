package com.voltforge.app.service;

import com.voltforge.app.model.CategoryModel;
import com.voltforge.app.respository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CatergorySeviceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryModel> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(CategoryModel category) {
        categoryRepository.save(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        CategoryModel category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with categoryId: " + categoryId +" not found"));

        categoryRepository.delete(category);

        return "Category with categoryId: " + categoryId + " has been deleted";
    }

    @Override
    public CategoryModel updateCategory(Long categoryId,  CategoryModel category) {
        categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with categoryId: " + categoryId + " not found"));

        category.setCategoryId(categoryId);

        return categoryRepository.save(category);
    }
}
