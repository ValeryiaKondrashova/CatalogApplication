package com.example.catalog.repository;

import com.example.catalog.models.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepo extends CrudRepository<Category, Long> {
    Category findCategoryByNameCategory(String category);
}
