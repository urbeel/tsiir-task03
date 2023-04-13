package by.urbel.task03.controller.command.impl;

import by.urbel.task03.controller.Pages;
import by.urbel.task03.controller.command.Command;
import by.urbel.task03.controller.command.RequestParams;
import by.urbel.task03.entity.Product;
import by.urbel.task03.entity.ProductCategory;
import by.urbel.task03.entity.User;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.service.ProductService;
import by.urbel.task03.service.ServiceProvider;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class CreateProductCommand implements Command {
    private final ProductService productService = ServiceProvider.getInstance().getProductService();

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute(RequestParams.USER);
        if (user == null || !user.getRole().equals(Role.ADMIN)) {
            LOGGER.warn("Not admin wanted to create product");
            response.sendRedirect(request.getContextPath());
            return;
        }
        Part photoPart = request.getPart(RequestParams.PRODUCT_PHOTO);
        String name = request.getParameter(RequestParams.PRODUCT_NAME);
        long categoryId = Long.parseLong(request.getParameter(RequestParams.PRODUCT_CATEGORY_ID));
        String description = request.getParameter(RequestParams.PRODUCT_DESCRIPTION);
        long price = Long.parseLong(request.getParameter(RequestParams.PRODUCT_PRICE));
        int quantity = Integer.parseInt(request.getParameter(RequestParams.PRODUCT_QUANTITY));
        Product product = new Product();
        ProductCategory category = new ProductCategory();
        category.setId(categoryId);
        product.setName(name);
        product.setCategory(category);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        try {
            productService.save(product, photoPart);
            response.sendRedirect(request.getContextPath() + Pages.PRODUCTS);
        } catch (ServiceException e) {
            request.setAttribute(RequestParams.ERROR, e.getMessage());
            request.getRequestDispatcher(Pages.PRODUCTS).forward(request, response);
        }
    }
}
