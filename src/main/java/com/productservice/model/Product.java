package com.productservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "products")
public class Product {

	public Product(Integer productId, String productName, Integer productPrice) {
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
	}

	@Id
	@Column(name = "product_id")
	private Integer productId;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "product_price")
	private Integer productPrice;

	
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductPrice(Integer productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getProductPrice() {
		return productPrice;
	}

}