package com.voltforge.app.repository;

import com.voltforge.app.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {
    CategoryModel findByCategoryName(String categoryName);
}
