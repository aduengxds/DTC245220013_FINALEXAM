<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.codegym.model.Product" %>
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
    Product product = (Product) request.getAttribute("product");
    boolean editing = product != null && product.getId() > 0;
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= editing ? "Sửa sản phẩm" : "Thêm sản phẩm" %></title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="container narrow">
    <section class="panel">
        <h1><%= editing ? "Sửa sản phẩm" : "Thêm sản phẩm mới" %></h1>

        <% if (error != null) { %>
            <div class="alert error"><%= escapeHtml(error) %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/products" method="post">
            <% if (editing) { %>
                <input type="hidden" name="id" value="<%= product.getId() %>">
            <% } %>

            <label for="name">Tên sản phẩm</label>
            <input id="name" name="name" type="text" maxlength="255"
                   required value="<%= product == null ? "" : escapeHtml(product.getName()) %>">

            <label for="price">Giá (VNĐ)</label>
            <input id="price" name="price" type="number" min="0" step="0.01"
                   required value="<%= product == null || product.getPrice() == null ? "" : product.getPrice().toPlainString() %>">

            <label for="quantity">Số lượng</label>
            <input id="quantity" name="quantity" type="number" min="0"
                   required value="<%= product == null ? "" : product.getQuantity() %>">

            <div class="form-actions">
                <button class="button" type="submit">Lưu</button>
                <a class="button secondary" href="<%= request.getContextPath() %>/products">Hủy</a>
            </div>
        </form>
    </section>
</div>
</body>
</html>