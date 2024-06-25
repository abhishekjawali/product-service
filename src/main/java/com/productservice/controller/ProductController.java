package com.productservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.productservice.model.Product;
import com.productservice.service.ProductService;

@RestController
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping(path = "/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productService.getProduct(id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }



    @GetMapping(path = "/products")
    public ResponseEntity<List<Product>> getProducts(
        @RequestParam(defaultValue = "0") int pageNumber,
        @RequestParam(defaultValue = "10") int pageSize) {
            Page<Product> products = productService.getAllProducts(pageNumber, pageSize);
            List<Product> productList = products.getContent();
            return new ResponseEntity<>(productList, HttpStatus.OK);
    }


    @GetMapping(path = "/healthz")
    public ResponseEntity<String> healthz() {
        return new ResponseEntity<String>("App is running!", HttpStatus.OK);
    }
}
