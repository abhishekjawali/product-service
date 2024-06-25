DROP TABLE IF EXISTS products;
 
CREATE TABLE products (
  product_id INT PRIMARY KEY,
  product_name VARCHAR(250) NOT NULL,
  product_price INT NOT NULL
  
);

INSERT INTO products (product_id, product_name, product_price) VALUES
  (1, 'Product-1', 100),
  (2, 'Product-2', 120),
  (3, 'Product-3', 200),
  (4, 'Product-4', 900);