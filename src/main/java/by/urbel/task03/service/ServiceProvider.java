package by.urbel.task03.service;

import by.urbel.task03.service.impl.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ServiceProvider {
    private static final ServiceProvider INSTANCE = new ServiceProvider();

    private final CartService cartService = new CartServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();
    private final OrderStatusService orderStatusService = new OrderStatusServiceImpl();
    private final ProductCategoryService productCategoryService = new ProductCategoryServiceImpl();
    private final ProductService productService = new ProductServiceImpl();
    private final RoleService roleService = new RoleServiceImpl();
    private final UserService userService = new UserServiceImpl();

    public static ServiceProvider getInstance() {
        return INSTANCE;
    }
}
