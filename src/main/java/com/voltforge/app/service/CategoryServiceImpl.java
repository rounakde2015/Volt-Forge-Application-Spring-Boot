package com.voltforge.app.service;

import com.voltforge.app.exception.APIException;
import com.voltforge.app.exception.ResourceNotFoundException;
import com.voltforge.app.model.Category;
import com.voltforge.app.payload.CategoryDTO;
import com.voltforge.app.payload.CategoryResponse;
import com.voltforge.app.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,  String sortBy, String sortOrder) {
        Sort sortCategoryOrderAndBy = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortCategoryOrderAndBy);
        Page<Category>  categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> allCategories = categoryPage.getContent();

        if (allCategories.isEmpty()) {
            throw new APIException("No Categories created !!!");
        }

        List<CategoryDTO> categoryDTOS = allCategories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setCategories(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);

        Category existingCategoryFromDB = categoryRepository.findByCategoryName((category.getCategoryName()));

        if (existingCategoryFromDB != null) {
            throw new APIException("Category with the name " + category.getCategoryName() + " already exists");
        }

        Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);

    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository
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

        Category category =  modelMapper.map(categoryDTO, Category.class);

        category.setCategoryId(categoryId);

        return modelMapper.map(categoryRepository.save(category), CategoryDTO.class);
    }
}
