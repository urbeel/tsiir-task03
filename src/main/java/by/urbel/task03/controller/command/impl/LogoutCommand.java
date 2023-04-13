package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LogoutCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().removeAttribute(RequestParams.USER);
        response.sendRedirect(request.getContextPath());
    }
}
