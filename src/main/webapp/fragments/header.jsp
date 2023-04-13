<%@ page import="by.urbel.task03.entity.enums.Role" %>

<nav class="navbar bg-dark navbar-dark navbar-expand">
    <div class="container d-flex">
        <a class="navbar-brand" href="${pageContext.request.contextPath}">Store</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarNav">
            <ul class="navbar-nav">
                <c:choose>
                    <c:when test="${sessionScope.user == null}">
                        <li class="nav-item">
                            <a class="nav-link" href="<c:url value="/pages/login.jsp"/>">SIGN IN</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${sessionScope.user.role.equals(Role.CUSTOMER)}">
                            <li class="nav-item">
                                <a class="nav-link" href="<c:url value="/pages/cart.jsp"/>">CART</a>
                            </li>
                        </c:if>
                        <li class="nav-item">
                            <form method="post" action="<c:url value="/controller?command=logout"/>">
                                <button type="submit" class="nav-link">LOGOUT</button>
                            </form>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>