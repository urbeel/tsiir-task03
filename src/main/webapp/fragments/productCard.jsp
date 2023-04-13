<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="by.urbel.task03.entity.enums.Role" %>

<jsp:useBean id="currentProduct" scope="request" type="by.urbel.task03.entity.Product"/>

<div class="col">
    <div class="card h-100">
        <a href="<c:url value="/controller?command=get_product&product-id=${currentProduct.id}"/>"
           class="text-decoration-none text-dark">
            <img src="${currentProduct.photoUrl}" class="card-img-top" height="200" alt="...">
            <div class="card-body">
                <h5 class="card-title">${currentProduct.name}</h5>
                <p class="card-text">${currentProduct.category.name}</p>
            </div>
        </a>
        <div class="card-footer">
            <strong class="text-body-secondary">
                <fmt:formatNumber value="${currentProduct.price / 100}"
                                  pattern="#,###.## $" minFractionDigits="2"
                                  type="currency"
                                  currencySymbol="$"/>
            </strong>
            <c:if test="${sessionScope.user!=null && sessionScope.user.role.equals(Role.ADMIN)}">
                <p class="card-text">Quantity: ${currentProduct.quantity}</p>
                <div class="row">
                    <div class="col-6">
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                data-bs-target="#quantity${currentProduct.id}Modal">Quantity
                        </button>
                    </div>
                    <div class="col-6">
                        <form method="post" action="<c:url value="/controller?command=delete_product"/>">
                            <input type="hidden" name="product-id" value="${currentProduct.id}">
                            <input type="hidden" name="photo-url" value="${currentProduct.photoUrl}">
                            <button type="submit" class="btn btn-danger">DELETE</button>
                        </form>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
    <div class="modal fade" id="quantity${currentProduct.id}Modal" tabindex="-1" aria-labelledby="quantityModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form method="post" action="<c:url value="/controller?command=change_product_quantity"/>">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="quantityModalLabel">CHANGE PRODUCT QUANTITY</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="product-id" value="${currentProduct.id}">
                        <label for="p-quantity" class="form-label">NEW PRODUCT QUANTITY:</label>
                        <input type="number" name="product-quantity" class="form-control mb-2" id="p-quantity" min="0">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Change</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>