package com.voltforge.app.repository;

import com.voltforge.app.model.Category;
import com.voltforge.app.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryModel(Category category);
    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}
