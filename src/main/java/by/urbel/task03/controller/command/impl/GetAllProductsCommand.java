package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.Product;
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
import java.util.List;

public class GetAllProductsCommand implements Command {
    private final ProductService productService = ServiceProvider.getInstance().getProductService();

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(RequestParams.USER);
        if (user == null || !user.getRole().equals(Role.ADMIN)) {
            LOGGER.warn("Not admin wanted to get all products");
            return;
        }
        try {
            List<Product> products = productService.readAll();
            request.setAttribute(RequestParams.PRODUCTS, products);
        } catch (ServiceException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
        }
    }
}
