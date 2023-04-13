package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.Pages;
import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.Cart;
import by.urbel.task03.entity.Order;
import by.urbel.task03.entity.User;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.service.CartService;
import by.urbel.task03.service.OrderService;
import by.urbel.task03.service.ServiceProvider;
import by.urbel.task03.service.exception.CartNeedUpdateException;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CreateOrderCommand implements Command {
    private final OrderService orderService = ServiceProvider.getInstance().getOrderService();
    private final CartService cartService = ServiceProvider.getInstance().getCartService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(RequestParams.USER);
        if (user == null || !user.getRole().equals(Role.CUSTOMER)) {
            response.sendRedirect(request.getContextPath());
            return;
        }
        try {
            Cart cart = cartService.readById(user.getCartId());
            if (cart == null) {
                request.setAttribute(RequestParams.ERROR, "Cart not found");
                request.getRequestDispatcher(Pages.CART).forward(request, response);
                return;
            }
            Order order = new Order();
            order.setUser(user);
            order.setTotalPrice(cart.getTotalPrice());
            order.setItems(cart.getItems());
            orderService.create(order);
            response.sendRedirect(request.getContextPath() + Pages.CART);
        } catch (ServiceException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
            request.getRequestDispatcher(Pages.CART).forward(request, response);
        } catch (CartNeedUpdateException e) {
            request.setAttribute(RequestParams.ERROR, "Order rejected. " +
                    "The quantity of products in the cart has been changed based on stock. " +
                    "Check your cart and try again.");
            request.getRequestDispatcher(Pages.CART).forward(request, response);
        }
    }
}
