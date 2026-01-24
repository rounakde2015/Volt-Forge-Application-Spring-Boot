package com.voltforge.app.service;

import com.voltforge.app.exception.APIException;
import com.voltforge.app.exception.ResourceNotFoundException;
import com.voltforge.app.model.CategoryModel;
import com.voltforge.app.payload.CategoryDTO;
import com.voltforge.app.payload.CategoryResponse;
import com.voltforge.app.respository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatergorySeviceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<CategoryModel> allCategories = categoryRepository.findAll();

        if (allCategories.isEmpty()) {
            throw new APIException("No Categories created !!!");
        }

        List<CategoryDTO> categoryDTOS = allCategories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);

        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        CategoryModel categoryModel = modelMapper.map(categoryDTO, CategoryModel.class);

        CategoryModel existingCategoryFromDB = categoryRepository.findByCategoryName((categoryModel.getCategoryName()));

        if (existingCategoryFromDB != null) {
            throw new APIException("Category with the name " + categoryModel.getCategoryName() + " already exists");
        }

        CategoryModel savedCategory = categoryRepository.save(categoryModel);

        return modelMapper.map(savedCategory, CategoryDTO.class);

    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        CategoryModel category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category",  "CategoryId", categoryId));

        categoryRepository.delete(category);

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId,  CategoryDTO categoryDTO) {
        categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category",  "CategoryId", categoryId));

        CategoryModel category =  modelMapper.map(categoryDTO, CategoryModel.class);

        category.setCategoryId(categoryId);

        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
    }
}
