package com.voltforge.app.service;

import com.voltforge.app.model.CategoryModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CatergorySeviceImpl implements CategoryService {
    private List<CategoryModel> categories = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<CategoryModel> getAllCategories() {
        return categories;
    }

    @Override
    public void addCategory(CategoryModel category) {
        category.setCategoryId(nextId++);
        categories.add(category);

    }

    @Override
    public String deleteCategory(Long categoryId) {
        CategoryModel category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        categories.remove(category);
        return "Category with catergoryId: " + categoryId + " has been deleted";
    }

    @Override
    public String updateCategory(Long categoryId,  CategoryModel category) {
        Optional<CategoryModel> optionalCategory = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();
        if (optionalCategory.isPresent()) {
            CategoryModel categoryToUpdate = optionalCategory.get();
            categoryToUpdate.setCategoryName(category.getCategoryName());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        return "Category with categoryId: " + categoryId + " has been updated";
    }
}
