package com.productservice.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.productservice.model.Product;


public class ProductRepository  {
    private static final String DB_CONNECTION = System.getenv("DB_CONNECTION_URL");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);
    private static Connection connection;

    static {
        logger.info("START: Database Connection initialization");
        connection = getDatabaseConnection();
        logger.info("END: Database Connection initialization");
    }

    private static Connection getDatabaseConnection() {
        try {
            if (connection == null || !connection.isValid(1)) {
            	// we will just retry once here to keep it simple
            	// for prod systems, use retries with exponential backoff
                logger.info("database credentials {}, {}, {}", DB_CONNECTION, DB_USER, DB_PASSWORD);

            	connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            logger.error("Error while connecting to the database", e);
        }
        return connection;
    }

    public Optional<List<Product>> findAllProducts() {
        var connection = getDatabaseConnection();
        List<Product> productList = new ArrayList<Product>();
        try {
            var statement = connection.prepareStatement("Select * from products");
            var result = statement.executeQuery();

            if(result.next()){
                var product = new Product();
                product.setProductId(result.getInt("product_id"));
                product.setProductName(result.getString("product_name"));
                product.setProductPrice(result.getInt("product_price"));
                productList.add(product);
                
                
            }
            
        } catch (SQLException sqlException) {
            var errorMsg = "Error while retrieving products";
            logger.error(errorMsg, sqlException);
            throw new RuntimeException(errorMsg, sqlException);
        }
        return Optional.of(productList);
    }

    public Optional<Product> findById(Integer productId) {
        logger.info("Inside find yb ID with the below ID {}", productId);
        
        var connection = getDatabaseConnection();
        try {
            var statement = connection.prepareStatement("Select * from products where product_id = ?");
            statement.setInt(1, productId);
            var result = statement.executeQuery();

            if(result.next()){
                var product = new Product();
                product.setProductId(result.getInt("product_id"));
                product.setProductName(result.getString("product_name"));
                product.setProductPrice(result.getInt("product_price"));
                
                return Optional.of(product);
            }
            return Optional.empty();
        } catch (SQLException sqlException) {
            var errorMsg = "Error while retrieving products";
            logger.error(errorMsg, sqlException);
            throw new RuntimeException(errorMsg, sqlException);
        }
    }

}