package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.Pages;
import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.User;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.service.ServiceProvider;
import by.urbel.task03.service.UserService;
import by.urbel.task03.service.exception.BadCredentialsException;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RegisterCommand implements Command {
    private final UserService userService = ServiceProvider.getInstance().getUserService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        user.setEmail(request.getParameter(RequestParams.USER_EMAIL));
        user.setPassword(request.getParameter(RequestParams.USER_PASSWORD));
        user.setFirstname(request.getParameter(RequestParams.USER_FIRSTNAME));
        user.setLastname(request.getParameter(RequestParams.USER_LASTNAME));
        user.setPhone(request.getParameter(RequestParams.USER_PHONE));
        user.setAddress(request.getParameter(RequestParams.USER_ADDRESS));
        user.setRole(Role.CUSTOMER);
        try {
            userService.register(user);
            user = userService.authorize(user.getEmail(), user.getPassword());
            request.getSession().setAttribute(RequestParams.USER, user);
            response.sendRedirect(request.getContextPath());
        } catch (ServiceException | BadCredentialsException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
            request.getRequestDispatcher(Pages.REGISTRATION).forward(request, response);
        }
    }
}
