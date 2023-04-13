<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en" class="h-100">
<head>
    <title>Error</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
</head>
<body class="h-100">
<%@ include file="/fragments/header.jsp" %>
<div class="container h-75 mt-3 d-flex flex-column align-items-center justify-content-center">
    <h1>404 | NOT FOUND!</h1>
    <a href="${pageContext.request.contextPath}">Go to Home Page</a>
</div>
<br/>
</body>
</html>