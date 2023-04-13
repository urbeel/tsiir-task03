<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
</head>
<body style="height: 100%">
<%@ include file="../fragments/header.jsp" %>
<div class="container">
    <c:if test="${requestScope.error != null}">
        <div class="row col-5 alert alert-danger my-3 mx-auto" role="alert">
                ${requestScope.error}
        </div>
    </c:if>
    <div class="row card mt-5 p-5 mx-auto" style="width: 500px">
        <form method="post" action="<c:url value="/controller?command=authorize"/>">
            <h1 class="mb-3 text-center">LOGIN</h1>
            <label class="form-label" for="u-email">Email</label>
            <input type="text" name="email" id="u-email" class="form-control mb-2" required>
            <label class="form-label" for="u-pass">Password</label>
            <input type="password" name="password" id="u-pass" class="form-control mb-4" required>
            <div class="row d-flex justify-content-center">
                <button type="submit" class="btn btn-primary col-6">LOGIN</button>
            </div>
        </form>
        <span class="mt-3 text-center">Don't have account?<strong class="mx-2">
            <a href="${pageContext.request.contextPath}/pages/registration.jsp">Sign Up</a></strong>
        </span>
    </div>
</div>
</body>
</html>
