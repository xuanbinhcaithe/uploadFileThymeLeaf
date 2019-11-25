package com.codegym.service;

import com.codegym.model.Category;
import com.codegym.model.Product;

public interface ProductService {
    Iterable<Product> findAll();
    Product findById(Long id);
    void save(Product product);
    void remove(Long id);
    Iterable<Product> findAllByCategory(Category category);
    Iterable<Product> findAllByNameContaining(String name);
    Iterable<Product> findAllByPriceBetween(Double price, Double price2);

}
