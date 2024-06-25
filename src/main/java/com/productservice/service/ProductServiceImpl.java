package com.productservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.productservice.repository.ProductRepository;
import com.productservice.model.Product;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepo;

    @Override
    public Product getProduct(Integer productId) {
        logger.info("Retrieving product with id: {}", productId);
        return productRepo.findByProductId(productId);
    }

    @Override
    public List<Product> getAllProducts() {
        logger.info("Retrieving all products");
        List<Product> products = (List<Product>) productRepo.findAll();
        return products;
    }
}
