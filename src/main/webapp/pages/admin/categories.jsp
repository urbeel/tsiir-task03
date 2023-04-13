<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Categories</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
</head>
<body>
<%@ include file="/fragments/header.jsp" %>
<jsp:include page="/controller?command=get_all_categories"/>
<div class="container mt-3">
    <c:if test="${requestScope.error != null}">
        <div class="row col-5 alert alert-danger my-3 mx-auto" role="alert">
                ${requestScope.error}
        </div>
    </c:if>
    <div class="row mb-2">
        <div class="col-md-6">
            <form method="post" action="<c:url value="/controller?command=create_category"/>">
                <div class="row">
                    <label for="create-category-name" class="form-label">Product category name:</label>
                    <input type="text" name="category-name" class="form-control mb-2 col" id="create-category-name">
                    <div class="col">
                        <button type="submit" class="btn btn-success">ADD</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <c:choose>
        <c:when test="${not empty requestScope.categories}">
            <table class="table">
                <caption>Product categories</caption>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Category Name</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${requestScope.categories}" var="category">
                    <tr>
                        <td>${category.id}</td>
                        <td>${category.name}</td>
                        <td>
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                    data-bs-target="#updateCategory${category.id}Modal">UPDATE
                            </button>
                        </td>
                        <td>
                            <form method="post" action="<c:url value="/controller?command=delete_category"/>">
                                <input type="hidden" name="category-id" value="${category.id}">
                                <button type="submit" class="btn btn-danger">Delete</button>
                            </form>
                        </td>
                    </tr>
                    <div class="modal fade" id="updateCategory${category.id}Modal" tabindex="-1"
                         aria-labelledby="updateCategoryModalLabel"
                         aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form method="post" action="<c:url value="/controller?command=update_category"/>">
                                    <div class="modal-header">
                                        <h1 class="modal-title fs-5" id="updateCategoryModalLabel">Update
                                            category: ${category.name}</h1>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">

                                        <label for="c-name" class="form-label">Category name:</label>
                                        <input type="text" name="category-name" value="${category.name}"
                                               class="form-control" id="c-name">
                                        <input type="hidden" name="category-id" value="${category.id}">

                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close
                                        </button>
                                        <button type="submit" class="btn btn-primary">Update</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
    </c:choose>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe"
        crossorigin="anonymous"></script>
</body>
</html>