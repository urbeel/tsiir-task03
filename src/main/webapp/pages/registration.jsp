<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en" style="height: 100%">
<head>
    <title>Registration</title>
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
    <div class="row col-5 alert alert-danger my-3 mx-auto" role="alert" id="passwords-alert" style="display: none">
        Your passwords do not match.
    </div>
    <div class="row card mt-5 p-5 mx-auto" style="width: 500px">
        <h1 class="mb-3 text-center">SIGN UP</h1>
        <form method="post" action="<c:url value="/controller?command=register"/>"
              onsubmit="validatePasswords(this);return false;">
            <label class="form-label" for="u-name">Email</label>
            <input type="email" name="email"
                   id="u-name" class="form-control mb-2" required>

            <label class="form-label" for="u-firstname">First name</label>
            <input type="text" name="firstname"
                   id="u-firstname" class="form-control mb-2" required>

            <label class="form-label" for="u-lastname">Last name</label>
            <input type="text" name="lastname"
                   id="u-lastname" class="form-control mb-2" required>

            <label class="form-label" for="u-phone">Phone number</label>
            <input type="text" name="phone"
                   id="u-phone" class="form-control mb-2" required>

            <label class="form-label" for="u-address">Address</label>
            <input type="text" name="address"
                   id="u-address" class="form-control mb-2" required>

            <label class="form-label" for="pass">Password</label>
            <input type="password" name="password"
                   id="pass" class="form-control mb-2" required>

            <label class="form-label" for="confirm-pass">Confirm password</label>
            <input type="password" id="confirm-pass" class="form-control mb-3" required>
            <div class="d-flex justify-content-center">
                <button type="submit" class="btn btn-primary">
                    SIGN UP
                </button>
            </div>
        </form>
        <span class="mt-4 text-center">Already have account?<strong class="mx-2">
            <a href="${pageContext.request.contextPath}/pages/login.jsp">Log in</a></strong>
        </span>
    </div>
</div>
<script src="<c:url value="/js/passwordUtils.js"/>"></script>
</body>
</html>
