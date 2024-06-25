package com.productservice.service;

import java.util.List;

import com.productservice.model.Product;

public interface ProductService {

	public Product getProduct(Integer productId);

	public List<Product> getAllProducts();

}