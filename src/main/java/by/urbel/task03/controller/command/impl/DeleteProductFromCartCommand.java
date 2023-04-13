package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.Pages;
import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.Item;
import by.urbel.task03.entity.Product;
import by.urbel.task03.entity.User;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.service.CartService;
import by.urbel.task03.service.ServiceProvider;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DeleteProductFromCartCommand implements Command {
    private final CartService cartService = ServiceProvider.getInstance().getCartService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(RequestParams.USER);
        if (user == null || !user.getRole().equals(Role.CUSTOMER)) {
            response.sendRedirect(request.getContextPath());
            return;
        }
        int itemQuantity = Integer.parseInt(request.getParameter(RequestParams.ITEM_QUANTITY));
        long productId = Long.parseLong(request.getParameter(RequestParams.PRODUCT_ID));
        long productPrice = Long.parseLong(request.getParameter(RequestParams.PRODUCT_PRICE));
        Product product = new Product();
        product.setId(productId);
        product.setPrice(productPrice);
        Item item = new Item();
        item.setProduct(product);
        item.setQuantity(itemQuantity);
        try {
            cartService.removeProductFromCart(item, user.getCartId());
            response.sendRedirect(request.getContextPath() + Pages.CART);
        } catch (ServiceException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
            request.getRequestDispatcher(Pages.CART).forward(request, response);
        }
    }
}
