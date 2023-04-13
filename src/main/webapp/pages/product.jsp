<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>${requestScope.product.name}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
</head>
<body>
<%@ include file="/fragments/header.jsp" %>
<div class="container mt-3">
    <c:if test="${requestScope.error != null}">
        <div class="row col-5 alert alert-danger my-3 mx-auto" role="alert">
                ${requestScope.error}
        </div>
    </c:if>
    <div class="card mb-3">
        <div class="row g-0">
            <div class="col-md-4">
                <img src="${requestScope.product.photoUrl}" class="img-fluid rounded-start" alt="Product image">
            </div>
            <div class="col-md-8">
                <div class="card-body h-100 d-flex flex-column justify-content-around">
                    <h5 class="card-title"><strong>${requestScope.product.name}</strong></h5>
                    <span class="text-body-secondary">Category: ${requestScope.product.category.name}</span>
                    <c:if test="${requestScope.product.description!=null}">
                        <p class="text-body-secondary">
                            Description:<br>
                                ${requestScope.product.description}</p>
                    </c:if>
                    <p class="card-text">Price:
                        <strong><fmt:formatNumber value="${requestScope.product.price / 100}"
                                                  pattern="#,###.## $" minFractionDigits="2"
                                                  type="currency"
                                                  currencySymbol="$"/>
                        </strong>
                    </p>
                    <p class="card-text">In Stock: <strong>${requestScope.product.quantity}</strong></p>
                    <c:if test="${sessionScope.user!=null && sessionScope.user.role.equals(Role.CUSTOMER)}">
                        <form method="post" action="<c:url value="/controller?command=add_product_to_cart"/>">
                            <input type="hidden" name="product-id" value="${requestScope.product.id}">
                            <div class="row row-cols-3 gx-5">
                                <div class="col">
                                    <div class="row">
                                        <label for="p-quantity" class="form-label col">Quantity:</label>
                                        <input name="product-quantity" type="number" id="p-quantity"
                                               class="form-control col"
                                               style="width: 100px;"
                                               min="1" max="${requestScope.product.quantity}" required>
                                    </div>
                                </div>
                                <div class="col">
                                    <button class="btn btn-success align-self-center align-self-sm-start"
                                            style="width: 200px">
                                        Add To Cart
                                    </button>
                                </div>
                            </div>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
<br/>
</body>
</html>