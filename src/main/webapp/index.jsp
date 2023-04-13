<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="by.urbel.task03.entity.enums.StatusName" %>

<!DOCTYPE html>
<html lang="en">
<head style="height: 100%">
    <title>Online store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
</head>
<body style="height: 100%">
<%@ include file="/fragments/header.jsp" %>
<jsp:include page="/controller?command=get_products_in_stock"/>
<div class="container mt-3">
    <c:choose>
        <c:when test="${sessionScope.user!=null && sessionScope.user.role.equals(Role.ADMIN)}">
            <h1 class="mb-4">Admin panel</h1>
            <ul class="nav nav-underline">
                <li class="nav-item">
                    <a href="<c:url value="/pages/admin/categories.jsp"/>" class="nav-link">PRODUCT CATEGORIES</a>
                </li>
                <li class="nav-item">
                    <a href="<c:url value="/pages/admin/products.jsp"/>" class="nav-link">PRODUCTS</a>
                </li>
                <li class="nav-item">
                    <a href="<c:url value="/pages/admin/users.jsp"/>" class="nav-link">USERS</a>
                </li>
                <li class="nav-item">
                    <a href="<c:url value="/controller?command=get_orders_by_status&status=${StatusName.NEW}"/>"
                       class="nav-link">NEW ORDERS</a>
                </li>
                <li class="nav-item">
                    <a href="<c:url value="/controller?command=get_orders_by_status&status=${StatusName.ACCEPTED}"/>"
                       class="nav-link">ACCEPTED ORDERS</a>
                </li>
                <li class="nav-item">
                    <a href="<c:url value="/controller?command=get_orders_by_status&status=${StatusName.FINISHED}"/>"
                       class="nav-link">FINISHED ORDERS</a>
                </li>
                <li class="nav-item">
                    <a href="<c:url value="/controller?command=get_orders_by_status&status=${StatusName.CANCELED}"/>"
                       class="nav-link">CANCELED ORDERS</a>
                </li>
            </ul>
        </c:when>
        <c:otherwise>
            <c:if test="${not empty requestScope.products}">
                <div class="row row-cols-1 row-cols-lg-5 row-cols-md-3 g-4">
                    <c:forEach items="${requestScope.products}" var="product">
                        <c:set var="currentProduct" value="${product}" scope="request"/>
                        <jsp:include page="/fragments/productCard.jsp"/>
                    </c:forEach>
                </div>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>
<br/>
</body>
</html>