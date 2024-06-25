package com.productservice.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.productservice.model.Product;

public interface ProductService {

	public Product getProduct(Integer productId);

	public Page<Product> getAllProducts(int pageNumber, int pageSize);

}