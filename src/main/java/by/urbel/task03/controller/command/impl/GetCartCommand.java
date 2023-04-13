package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.Cart;
import by.urbel.task03.entity.User;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.service.CartService;
import by.urbel.task03.service.ServiceProvider;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GetCartCommand implements Command {
    private final CartService cartService = ServiceProvider.getInstance().getCartService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(RequestParams.USER);
        if (user == null || !user.getRole().equals(Role.CUSTOMER)) {
            return;
        }
        try {
            Cart cart = cartService.readById(user.getCartId());
            request.setAttribute(RequestParams.CART, cart);
        } catch (ServiceException e) {
            request.setAttribute(RequestParams.ERROR, "Cannot load cart");
        }
    }
}
