package com.voltforge.app.respository;

import com.voltforge.app.model.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryModel, Long> {

}
