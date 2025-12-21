package com.voltforge.app.service;

import com.voltforge.app.model.CategoryModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatergorySeviceImpl implements CategoryService {
    private List<CategoryModel> categories = new ArrayList<>();

    @Override
    public List<CategoryModel> getAllCategories() {
        return categories;
    }

    @Override
    public void addCategory(CategoryModel category) {
        categories.add(category);

    }
}
