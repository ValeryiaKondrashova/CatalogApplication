package com.example.catalog.repository;

import com.example.catalog.models.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepo extends CrudRepository<Product, Long> {
    Product findProductByNameProduct(String nameProduct);
}
