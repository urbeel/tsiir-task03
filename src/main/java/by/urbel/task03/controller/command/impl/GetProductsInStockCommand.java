package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.Product;
import by.urbel.task03.service.ProductService;
import by.urbel.task03.service.ServiceProvider;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class GetProductsInStockCommand implements Command {
    private final ProductService productService = ServiceProvider.getInstance().getProductService();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Product> products = productService.readInStock();
            request.setAttribute(RequestParams.PRODUCTS, products);
        } catch (ServiceException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
        }
    }
}
