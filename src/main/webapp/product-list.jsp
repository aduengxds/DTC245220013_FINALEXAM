<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,com.codegym.model.Product" %>
<%!
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
%>
<%
    List<Product> products = (List<Product>) request.getAttribute("products");
    String message = request.getParameter("message");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý sản phẩm</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <div>
            <h1>Quản lý sản phẩm</h1>
            <p>Final Exam - JSP, Servlet, JDBC và MySQL</p>
        </div>
        <a class="button" href="<%= request.getContextPath() %>/products?action=new">+ Thêm sản phẩm</a>
    </header>

    <% if ("saved".equals(message)) { %>
        <div class="alert success">Lưu sản phẩm thành công.</div>
    <% } else if ("deleted".equals(message)) { %>
        <div class="alert success">Đã xóa sản phẩm.</div>
    <% } %>

    <section class="panel">
        <h2>Danh sách sản phẩm</h2>
        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên sản phẩm</th>
                    <th>Giá (VNĐ)</th>
                    <th>Số lượng</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <% if (products != null && !products.isEmpty()) {
                    for (Product product : products) { %>
                    <tr>
                        <td><%= product.getId() %></td>
                        <td><%= escapeHtml(product.getName()) %></td>
                        <td><%= product.getPrice().toPlainString() %></td>
                        <td><%= product.getQuantity() %></td>
                        <td class="actions">
                            <a class="button secondary"
                               href="<%= request.getContextPath() %>/products?action=edit&id=<%= product.getId() %>">Sửa</a>
                            <a class="button danger"
                               href="<%= request.getContextPath() %>/products?action=delete&id=<%= product.getId() %>"
                               onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này?');">Xóa</a>
                        </td>
                    </tr>
                <%  }
                   } else { %>
                    <tr><td colspan="5" class="empty">Chưa có sản phẩm nào.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <p class="count">Tổng số sản phẩm: <strong><%= products == null ? 0 : products.size() %></strong></p>
    </section>
</div>
</body>
</html>