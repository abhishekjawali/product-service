package com.productservice.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productservice.model.Product;
import com.productservice.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepo;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Product getProduct(Integer productId) {
        Optional<Product> product = productRepo.findById(productId);
        return product.orElse(null);
    }

    @Override
    public List<Product> getAllProducts() {
        Optional<List<Product>> productList = productRepo.findAllProducts();
        return productList.orElse(List.of());
    }

}
