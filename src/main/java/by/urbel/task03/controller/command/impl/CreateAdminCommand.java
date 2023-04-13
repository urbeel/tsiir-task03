package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.Pages;
import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.User;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.service.ServiceProvider;
import by.urbel.task03.service.UserService;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CreateAdminCommand implements Command {
    private final UserService userService = ServiceProvider.getInstance().getUserService();

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(RequestParams.USER);
        if (user == null || !user.getRole().equals(Role.ADMIN)) {
            LOGGER.warn("Not admin wanted to create new admin");
            response.sendRedirect(request.getContextPath());
            return;
        }
        User admin = new User();
        admin.setEmail(request.getParameter(RequestParams.USER_EMAIL));
        admin.setPassword(request.getParameter(RequestParams.USER_PASSWORD));
        admin.setFirstname(request.getParameter(RequestParams.USER_FIRSTNAME));
        admin.setLastname(request.getParameter(RequestParams.USER_LASTNAME));
        admin.setRole(Role.ADMIN);
        try {
            userService.register(admin);
            response.sendRedirect(request.getContextPath() + Pages.USERS);
        } catch (ServiceException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
            request.getRequestDispatcher(Pages.USERS).forward(request, response);
        }
    }
}
