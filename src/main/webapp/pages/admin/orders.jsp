<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="by.urbel.task03.entity.enums.StatusName" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Orders</title>
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
    <h2 class="mb-3">${requestScope.status} ORDERS</h2>
    <c:if test="${not empty requestScope.orders}">
        <jsp:useBean id="orders" type="java.util.List<by.urbel.task03.entity.Order>" scope="request"/>
        <c:forEach items="${orders}" var="order">
            <div class="card mb-2">
                <div class="card-header">
                    <div class="row">
                        <h5 class="col">
                            <fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </h5>
                        <h5 class="col">Email: ${order.user.email}</h5>
                        <h5 class="col">Phone: ${order.user.phone}</h5>
                        <h5 class="col">Address: ${order.user.address}</h5>
                        <h5 class="col">Full Name: ${order.user.firstname} ${order.user.lastname}</h5>
                    </div>
                </div>
                <div class="card-body">
                    <table class="table table-hover">
                        <caption>Ordered products</caption>
                        <thead class="table-header">
                        <tr>
                            <th>ID</th>
                            <th>Product Name</th>
                            <th>Quantity</th>
                            <th>Total price</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${order.items}" var="item">
                            <tr>
                                <td>${item.product.id}</td>
                                <td>${item.product.name}</td>
                                <td>${item.quantity}</td>
                                <td><fmt:formatNumber value="${item.quantity * item.product.price / 100}"
                                                      pattern="#,###.## $" minFractionDigits="2"
                                                      type="currency"
                                                      currencySymbol="$"/></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                        <tfoot>
                        <tr>
                            <td colspan="4" class="card-text text-end"><strong class="fs-5">Total Price:
                                <fmt:formatNumber value="${order.totalPrice / 100}"
                                                  pattern="#,###.## $" minFractionDigits="2"
                                                  type="currency"
                                                  currencySymbol="$"/>
                            </strong>
                            </td>
                        </tr>
                        </tfoot>
                    </table>
                </div>
                <c:if test="${requestScope.status.equals(StatusName.NEW) || requestScope.status.equals(StatusName.ACCEPTED)}">
                    <div class="card-footer d-flex justify-content-between">
                        <c:choose>
                            <c:when test="${order.status.name.equals(StatusName.NEW.name)}">
                                <form method="post" action="<c:url value="/controller?command=change_order_status"/>">
                                    <input type="hidden" name="order-id" value="${order.id}">
                                    <input type="hidden" name="status" value="${StatusName.ACCEPTED}">
                                    <input type="hidden" name="page-path" value="/pages/new-orders.jsp">
                                    <button type="submit" class="btn btn-success">Accept</button>
                                </form>
                            </c:when>
                            <c:when test="${order.status.name.equals(StatusName.ACCEPTED.name)}">
                                <form method="post" action="<c:url value="/controller?command=change_order_status"/>">
                                    <input type="hidden" name="order-id" value="${order.id}">
                                    <input type="hidden" name="status" value="FINISHED">
                                    <input type="hidden" name="page-path" value="/pages/accepted-orders.jsp">
                                    <button type="submit" class="btn btn-success">Finish</button>
                                </form>
                            </c:when>
                        </c:choose>
                        <c:if test="${order.status.name.equals(StatusName.NEW.name) || order.status.name.equals(StatusName.ACCEPTED.name)}">
                            <form method="post" action="<c:url value="/controller?command=change_order_status"/>">
                                <input type="hidden" name="order-id" value="${order.id}">
                                <input type="hidden" name="status" value="${StatusName.CANCELED}">
                                <input type="hidden" name="page-path" value="/pages/new-orders.jsp">
                                <button type="submit" class="btn btn-danger">Cancel</button>
                            </form>
                        </c:if>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </c:if>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe"
        crossorigin="anonymous"></script>
</body>
</html>
