<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Products</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
</head>
<body>
<%@ include file="/fragments/header.jsp" %>
<jsp:include page="/controller?command=get_all_products"/>
<jsp:include page="/controller?command=get_all_categories"/>
<div class="container mt-3">
    <c:if test="${requestScope.error != null}">
        <div class="row col-5 alert alert-danger my-3 mx-auto" role="alert">
                ${requestScope.error}
        </div>
    </c:if>
    <div class="mb-3">
        <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#createProductModal">Add product</button>
    </div>
    <c:if test="${not empty requestScope.products}">
        <div class="row row-cols-1 row-cols-lg-5 row-cols-md-3 g-4">
            <c:forEach items="${requestScope.products}" var="product">
                <c:set var="currentProduct" value="${product}" scope="request"/>
                <jsp:include page="/fragments/productCard.jsp"/>
            </c:forEach>
        </div>
    </c:if>
    <div class="modal fade" id="createProductModal" tabindex="-1" aria-labelledby="createProductModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form method="post" enctype="multipart/form-data"
                      action="<c:url value="/controller?command=create_product"/>">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="createProductModalLabel">New product</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label for="p-name" class="form-label">Name: </label>
                            <input type="text" name="product-name" id="p-name" class="form-control" required/>
                        </div>
                        <div class="mb-3">
                            <label for="p-price" class="form-label">Price (in cents):</label>
                            <input type="number" min="0" name="product-price" id="p-price" class="form-control"
                                   required/>
                        </div>
                        <div class="mb-3">
                            <label for="p-quantity" class="form-label">Quantity:</label>
                            <input type="number" min="0" name="product-quantity" id="p-quantity" class="form-control"
                                   required/>
                        </div>
                        <div class="mb-3">
                            <label for="p-category" class="form-label">Category:</label>
                            <c:choose>
                                <c:when test="${not empty requestScope.categories}">
                                    <select name="product-category-id" id="p-category" class="form-control" required>
                                        <c:forEach items="${requestScope.categories}" var="category">
                                            <option value="${category.id}">${category.name}</option>
                                        </c:forEach>
                                    </select>
                                </c:when>
                                <c:otherwise>
                                    <select name="product-category" id="p-category" class="form-control" disabled>
                                        <option>No categories. Add at least one for creating product</option>
                                    </select>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="mb-3">
                            <label for="p-photo" class="form-label">Product photo:</label>
                            <input type="file" name="product-photo" accept="image/*" id="p-photo" class="form-control"/>
                        </div>
                        <div class="mb-3">
                            <label for="p-description" class="form-label">Description:</label>
                            <textarea name="product-description" cols="200" class="form-control"
                                      id="p-description"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <c:choose>
                            <c:when test="${not empty requestScope.categories}">
                                <button type="submit" class="btn btn-success">Add</button>
                            </c:when>
                            <c:otherwise>
                                <button type="submit" class="btn btn-success" disabled>Add</button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<br/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe"
        crossorigin="anonymous"></script>
</body>
</html>