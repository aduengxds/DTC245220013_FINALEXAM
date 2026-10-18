CREATE DATABASE IF NOT EXISTS product_management
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE product_management;

CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    quantity INT NOT NULL
);

INSERT INTO products (name, price, quantity)
SELECT 'Laptop Dell', 15990000.00, 10
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Laptop Dell');

INSERT INTO products (name, price, quantity)
SELECT 'Ban phim co', 850000.00, 25
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Ban phim co');

INSERT INTO products (name, price, quantity)
SELECT 'Chuot khong day', 350000.00, 40
WHERE NOT EXISTS (SELECT 1 FROM products WHERE name = 'Chuot khong day');