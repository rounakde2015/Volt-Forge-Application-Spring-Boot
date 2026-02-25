package com.voltforge.app.respository;

import com.voltforge.app.model.CategoryModel;
import com.voltforge.app.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByCategoryModel(CategoryModel categoryModel);
    List<ProductModel> findByProductNameLikeIgnoreCase(String keyword);
}
