package com.codegym.dao;

import com.codegym.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final String URL =
        "jdbc:mysql://localhost:3306/product_management?useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, price, quantity FROM products ORDER BY id DESC";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                products.add(mapProduct(result));
            }
        }
        return products;
    }

    public Product findById(int id) throws SQLException {
        String sql = "SELECT id, name, price, quantity FROM products WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapProduct(result);
                }
            }
        }
        return null;
    }

    public void insert(Product product) throws SQLException {
        String sql = "INSERT INTO products(name, price, quantity) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.executeUpdate();
        }
    }

    public boolean update(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, price = ?, quantity = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setInt(4, product.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Product mapProduct(ResultSet result) throws SQLException {
        return new Product(
            result.getInt("id"),
            result.getString("name"),
            result.getBigDecimal("price"),
            result.getInt("quantity")
        );
    }
}