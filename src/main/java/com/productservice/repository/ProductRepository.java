package com.productservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.productservice.model.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    Product findByProductId(Integer id);
}