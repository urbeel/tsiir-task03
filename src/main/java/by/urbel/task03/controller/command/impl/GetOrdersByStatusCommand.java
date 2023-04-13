package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.Pages;
import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.Order;
import by.urbel.task03.entity.User;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.entity.enums.StatusName;
import by.urbel.task03.service.OrderService;
import by.urbel.task03.service.ServiceProvider;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class GetOrdersByStatusCommand implements Command {
    private final OrderService orderService = ServiceProvider.getInstance().getOrderService();
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(RequestParams.USER);
        if (user == null || !user.getRole().equals(Role.ADMIN)) {
            LOGGER.warn("Not admin wanted to get orders by status");
            response.sendRedirect(request.getContextPath());
            return;
        }
        StatusName statusName = StatusName.valueOf(request.getParameter(RequestParams.ORDER_STATUS));
        request.setAttribute(RequestParams.ORDER_STATUS, statusName);
        try {
            List<Order> orders = orderService.readOrdersByStatusName(statusName);
            request.setAttribute(RequestParams.ORDERS, orders);
        } catch (ServiceException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
        }
        request.getRequestDispatcher(Pages.ORDERS).forward(request, response);
    }
}
