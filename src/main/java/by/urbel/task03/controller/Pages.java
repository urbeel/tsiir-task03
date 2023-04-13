package by.urbel.task03.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Pages {
    public static final String CART = "/pages/cart.jsp";
    public static final String LOGIN = "/pages/login.jsp";
    public static final String REGISTRATION = "/pages/registration.jsp";
    public static final String USERS = "/pages/admin/users.jsp";
    public static final String PRODUCT = "/pages/product.jsp";
    public static final String PRODUCTS = "/pages/admin/products.jsp";
    public static final String CATEGORIES = "/pages/admin/categories.jsp";
    public static final String ORDERS = "/pages/admin/orders.jsp";
    public static final String ERROR = "/pages/error.jsp";
}
