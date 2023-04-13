<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Cart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
</head>
<body>
<%@ include file="/fragments/header.jsp" %>
<jsp:include page="/controller?command=get_cart"/>
<c:if test="${sessionScope.user==null || !sessionScope.user.role.equals(Role.CUSTOMER)}">
    <c:redirect url="/"/>
</c:if>
<div class="container px-5 mt-3">
    <c:if test="${requestScope.error != null}">
        <div class="row col-5 alert alert-danger my-3 mx-auto" role="alert">
                ${requestScope.error}
        </div>
    </c:if>
    <c:if test="${requestScope.cart!=null}">
        <jsp:useBean id="cart" type="by.urbel.task03.entity.Cart" scope="request"/>
        <div class="row row-cols-1 row-cols-md-2 mt-5 gx-5">
            <div class="col">
                <c:forEach items="${cart.items}" var="item">
                    <div class="card mb-3">
                        <div class="row g-0">
                            <div class="col-md-4">
                                <img src="${item.product.photoUrl}" width="200" class="img-fluid rounded-start"
                                     alt="Product image">
                            </div>
                            <div class="col-md-8">
                                <div class="card-body h-100 d-flex flex-column justify-content-around">
                                    <h5 class="card-title"><strong>${item.product.name}</strong></h5>
                                    <span class="text-body-secondary">Category: ${item.product.category.name}</span>
                                    <p class="card-text">Price:
                                        <strong><fmt:formatNumber value="${item.product.price * item.quantity / 100}"
                                                                  pattern="#,###.## $" minFractionDigits="2"
                                                                  type="currency"
                                                                  currencySymbol="$"/>
                                        </strong>
                                    </p>
                                    <p class="card-text">Quantity: <strong>${item.quantity}</strong></p>
                                    <form method="post"
                                          action="<c:url value="/controller?command=delete_product_from_cart"/>">
                                        <input type="hidden" name="product-id" value="${item.product.id}">
                                        <input type="hidden" name="product-price" value="${item.product.price}">
                                        <input type="hidden" name="item-quantity" value="${item.quantity}">
                                        <button type="submit" class="btn btn-danger">DELETE FROM CART</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="col">
                <h4 class="mb-3">Total price:
                    <fmt:formatNumber value="${cart.totalPrice/100}"
                                      minFractionDigits="2" currencySymbol="$" pattern="#,###.## $"/>
                </h4>
                <div>
                    <c:choose>
                        <c:when test="${not empty cart.items}">
                            <form method="post" action="<c:url value="/controller?command=create_order"/>">
                                <button type="submit" class="btn btn-primary" style="width: 200px">
                                    Order
                                </button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-primary" style="width: 200px" disabled>
                                Order
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>
