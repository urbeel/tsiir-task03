package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.Pages;
import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.ProductCategory;
import by.urbel.task03.entity.User;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.service.ProductCategoryService;
import by.urbel.task03.service.ServiceProvider;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CreateCategoryCommand implements Command {
    private final ProductCategoryService categoryService = ServiceProvider.getInstance().getProductCategoryService();

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(RequestParams.USER);
        if (user == null || !user.getRole().equals(Role.ADMIN)) {
            LOGGER.warn("Not admin wanted to create category");
            response.sendRedirect(request.getContextPath());
            return;
        }
        String categoryName = request.getParameter(RequestParams.CATEGORY_NAME);
        ProductCategory category = new ProductCategory(categoryName);
        try {
            categoryService.create(category);
            response.sendRedirect(request.getContextPath() + Pages.CATEGORIES);
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(Pages.CATEGORIES).forward(request, response);
        }
    }
}
