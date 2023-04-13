package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.Pages;
import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
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

public class DeleteCategoryCommand implements Command {
    private final ProductCategoryService categoryService = ServiceProvider.getInstance().getProductCategoryService();

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(RequestParams.USER);
        if (user == null || !user.getRole().equals(Role.ADMIN)) {
            LOGGER.warn("Not admin wanted to delete category");
            response.sendRedirect(request.getContextPath());
            return;
        }
        long categoryId = Long.parseLong(request.getParameter(RequestParams.CATEGORY_ID));
        try {
            categoryService.delete(categoryId);
            response.sendRedirect(request.getContextPath() + Pages.CATEGORIES);
        } catch (ServiceException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
            request.getRequestDispatcher(Pages.CATEGORIES).forward(request, response);
        }
    }
}
