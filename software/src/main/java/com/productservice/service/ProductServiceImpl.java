package com.productservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.productservice.repository.ProductRepository;
import com.productservice.model.Product;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }


    @Autowired
    private ProductRepository productRepo;

    @Override
    public Product getProduct(Integer productId) {
        var product = productRepo.findById(productId);
        return product.get();
    }

   
    @Override
    public List<Product> getAllProducts() {
        var productList = productRepo.findAllProducts();
        return productList.get();
    }

}
