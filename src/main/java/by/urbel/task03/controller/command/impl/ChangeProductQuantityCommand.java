package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.Pages;
import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.User;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.service.ProductService;
import by.urbel.task03.service.ServiceProvider;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ChangeProductQuantityCommand implements Command {
    private final ProductService productService = ServiceProvider.getInstance().getProductService();

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(RequestParams.USER);
        if (user == null || !user.getRole().equals(Role.ADMIN)) {
            LOGGER.warn("Not admin wanted to change product quantity");
            response.sendRedirect(request.getContextPath());
            return;
        }
        long productId = Long.parseLong(request.getParameter(RequestParams.PRODUCT_ID));
        int productQuantity = Integer.parseInt(request.getParameter(RequestParams.PRODUCT_QUANTITY));
        try {
            productService.changeProductQuantity(productId, productQuantity);
            response.sendRedirect(request.getContextPath() + Pages.PRODUCTS);
        } catch (ServiceException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
            request.getRequestDispatcher(Pages.PRODUCTS).forward(request, response);
        }
    }
}
