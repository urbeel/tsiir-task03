package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.Pages;
import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.User;
import by.urbel.task03.service.ServiceProvider;
import by.urbel.task03.service.UserService;
import by.urbel.task03.service.exception.BadCredentialsException;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthorizeCommand implements Command {
    private final UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter(RequestParams.USER_EMAIL);
        String password = request.getParameter(RequestParams.USER_PASSWORD);
        try {
            User user = userService.authorize(email, password);
            request.getSession().setAttribute(RequestParams.USER, user);
            response.sendRedirect(request.getContextPath());
        } catch (ServiceException | BadCredentialsException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
            request.getRequestDispatcher(Pages.LOGIN).forward(request, response);
        }
    }
}
