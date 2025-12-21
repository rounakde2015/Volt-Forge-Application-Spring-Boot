package com.voltforge.app.controller;

import com.voltforge.app.model.CategoryModel;
import com.voltforge.app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/api/v1/public/categories")
    public List<CategoryModel> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/api/v1/public/categories")
    public String addCategory(@RequestBody CategoryModel category) {
        categoryService.addCategory(category);
        return "Category added Successfully";
    }
}
