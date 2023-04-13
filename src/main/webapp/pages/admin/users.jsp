<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Users</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
</head>
<body>
<%@ include file="/fragments/header.jsp" %>
<jsp:include page="/controller?command=get_all_users"/>
<div class="container mt-3">
    <c:if test="${requestScope.error != null}">
        <div class="row col-5 alert alert-danger my-3 mx-auto" role="alert">
                ${requestScope.error}
        </div>
    </c:if>
    <div class="row mb-3">
        <div class="col text-end">
            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createAdminModal">
                ADD ADMIN
            </button>
        </div>
    </div>
    <div class="modal fade" id="createAdminModal" tabindex="-1" aria-labelledby="createAdminModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form method="post" onsubmit="validatePasswords(this);return false;"
                      action="<c:url value="/controller?command=create_admin"/>">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="createAdminModalLabel">Add admin</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row alert alert-danger my-3 mx-auto" role="alert" id="passwords-alert"
                             style="display: none">
                            Your passwords do not match.
                        </div>
                        <label class="form-label" for="u-name">Email</label>
                        <input type="text" name="email"
                               id="u-name" class="form-control mb-2" required>

                        <label class="form-label" for="u-firstname">First name</label>
                        <input type="text" name="firstname"
                               id="u-firstname" class="form-control mb-2" required>

                        <label class="form-label" for="u-lastname">Last name</label>
                        <input type="text" name="lastname"
                               id="u-lastname" class="form-control mb-2" required>
                        <label class="form-label" for="pass">Password</label>
                        <input type="password" name="password"
                               id="pass" class="form-control mb-2" required>

                        <label class="form-label" for="confirm-pass">Confirm password</label>
                        <input type="password" id="confirm-pass" class="form-control mb-3" required>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-success">Add</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <h2 class="mb-3">USERS</h2>
    <c:if test="${not empty requestScope.users}">
        <jsp:useBean id="users" type="java.util.List<by.urbel.task03.entity.User>" scope="request"/>
        <table class="table">
            <caption>Users</caption>
            <thead>
            <tr>
                <th>ID</th>
                <th>Email</th>
                <th>Firstname</th>
                <th>Lastname</th>
                <th>Phone</th>
                <th>Address</th>
                <th>Role</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="user">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.email}</td>
                    <td>${user.firstname}</td>
                    <td>${user.lastname}</td>
                    <td>${user.phone != null ? user.phone : "N/A"}</td>
                    <td>${user.address != null ? user.address : "N/A"}</td>
                    <td>${user.role}</td>
                    <td>
                        <form method="post" action="<c:url value="/controller?command=delete_user"/>">
                            <input type="hidden" name="user-id" value="${user.id}">
                            <button type="submit" class="btn btn-danger">Delete</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>
</body>
<script src="<c:url value="/js/passwordUtils.js"/>"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe"
        crossorigin="anonymous"></script>
</html>
