package com.codegym.controller;

import com.codegym.dao.ProductDAO;
import com.codegym.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null || action.isBlank()) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    request.getRequestDispatcher("/product-form.jsp").forward(request, response);
                    break;

                case "edit":
                    int editId = Integer.parseInt(request.getParameter("id"));
                    Product product = productDAO.findById(editId);
                    if (product == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm.");
                        return;
                    }
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("/product-form.jsp").forward(request, response);
                    break;

                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    productDAO.delete(deleteId);
                    response.sendRedirect(request.getContextPath() + "/products?message=deleted");
                    break;

                default:
                    request.setAttribute("products", productDAO.findAll());
                    request.getRequestDispatcher("/product-list.jsp").forward(request, response);
                    break;
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ.");
        } catch (SQLException e) {
            throw new ServletException("Lỗi truy vấn cơ sở dữ liệu.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String idText = request.getParameter("id");
            String name = request.getParameter("name");
            String priceText = request.getParameter("price");
            String quantityText = request.getParameter("quantity");

            if (name == null || name.trim().isEmpty()
                    || priceText == null || quantityText == null) {
                request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
                request.setAttribute("product", new Product(
                    idText == null || idText.isBlank() ? 0 : Integer.parseInt(idText),
                    name == null ? "" : name,
                    BigDecimal.ZERO,
                    0
                ));
                request.getRequestDispatcher("/product-form.jsp").forward(request, response);
                return;
            }

            BigDecimal price = new BigDecimal(priceText);
            int quantity = Integer.parseInt(quantityText);

            if (price.compareTo(BigDecimal.ZERO) < 0 || quantity < 0) {
                throw new IllegalArgumentException("Giá và số lượng không được âm.");
            }

            if (price.scale() > 2) {
                throw new IllegalArgumentException("Giá chỉ được có tối đa 2 chữ số thập phân.");
            }

            Product product = new Product(name.trim(), price, quantity);

            if (idText != null && !idText.isBlank()) {
                product.setId(Integer.parseInt(idText));
                productDAO.update(product);
            } else {
                productDAO.insert(product);
            }

            response.sendRedirect(request.getContextPath() + "/products?message=saved");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Giá, số lượng hoặc ID không đúng định dạng.");
            request.getRequestDispatcher("/product-form.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/product-form.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Không thể lưu sản phẩm.", e);
        }
    }
}