package by.urbel.task03.controller.command;

import by.urbel.task03.controller.command.impl.*;

import java.util.EnumMap;
import java.util.Map;

public class CommandProvider {
    private final Map<CommandName, Command> commands = new EnumMap<>(CommandName.class);

    public CommandProvider() {
        commands.put(CommandName.AUTHORIZE, new AuthorizeCommand());
        commands.put(CommandName.REGISTER, new RegisterCommand());
        commands.put(CommandName.LOGOUT, new LogoutCommand());
        commands.put(CommandName.GET_ALL_PRODUCTS, new GetAllProductsCommand());
        commands.put(CommandName.GET_PRODUCTS_IN_STOCK, new GetProductsInStockCommand());
        commands.put(CommandName.GET_CART, new GetCartCommand());
        commands.put(CommandName.GET_PRODUCT, new GetProductCommand());
        commands.put(CommandName.CREATE_PRODUCT, new CreateProductCommand());
        commands.put(CommandName.DELETE_PRODUCT, new DeleteProductCommand());
        commands.put(CommandName.CHANGE_PRODUCT_QUANTITY, new ChangeProductQuantityCommand());
        commands.put(CommandName.ADD_PRODUCT_TO_CART, new AddProductToCartCommand());
        commands.put(CommandName.DELETE_PRODUCT_FROM_CART, new DeleteProductFromCartCommand());
        commands.put(CommandName.GET_ALL_CATEGORIES, new GetAllCategoriesCommand());
        commands.put(CommandName.CREATE_CATEGORY, new CreateCategoryCommand());
        commands.put(CommandName.DELETE_CATEGORY, new DeleteCategoryCommand());
        commands.put(CommandName.UPDATE_CATEGORY, new UpdateCategoryCommand());
        commands.put(CommandName.CREATE_ORDER, new CreateOrderCommand());
        commands.put(CommandName.GET_ORDERS_BY_STATUS, new GetOrdersByStatusCommand());
        commands.put(CommandName.CHANGE_ORDER_STATUS, new ChangeOrderStatusCommand());
        commands.put(CommandName.CREATE_ADMIN, new CreateAdminCommand());
        commands.put(CommandName.GET_ALL_USERS, new GetAllUsersCommand());
        commands.put(CommandName.DELETE_USER, new DeleteUserCommand());
    }

    public Command takeCommand(String command) {
        try {
            CommandName commandName = CommandName.valueOf(command.toUpperCase());
            return commands.get(commandName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
