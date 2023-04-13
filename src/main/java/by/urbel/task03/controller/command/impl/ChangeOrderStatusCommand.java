package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
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

public class ChangeOrderStatusCommand implements Command {
    private final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(RequestParams.USER);
        if (user == null || !user.getRole().equals(Role.ADMIN)) {
            LOGGER.warn("Not admin wanted to change order status");
            response.sendRedirect(request.getContextPath());
            return;
        }
        long orderId = Long.parseLong(request.getParameter(RequestParams.ORDER_ID));
        String orderStatus = request.getParameter(RequestParams.ORDER_STATUS);
        StatusName statusName = StatusName.valueOf(orderStatus);
        String pagePath = request.getParameter(RequestParams.PAGE_PATH);
        String referer = request.getHeader("Referer");
        try {
            orderService.changeOrderStatus(orderId, statusName);
            response.sendRedirect(referer);
        } catch (ServiceException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
            request.getRequestDispatcher(pagePath).forward(request, response);
        }
    }
}
