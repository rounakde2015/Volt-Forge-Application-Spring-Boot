package com.voltforge.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "volt-forge-categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;

    @NotNull
    @NotBlank
    @Size(min = 5, message = "Category name must contain atleast 5 characters.")
    private String categoryName;
}
