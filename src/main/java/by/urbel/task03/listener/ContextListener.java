package by.urbel.task03.listener;

import by.urbel.task03.listener.exception.ListenerException;
import by.urbel.task03.service.OrderStatusService;
import by.urbel.task03.service.RoleService;
import by.urbel.task03.service.UserService;
import by.urbel.task03.service.exception.ServiceException;
import by.urbel.task03.service.impl.OrderStatusServiceImpl;
import by.urbel.task03.service.impl.RoleServiceImpl;
import by.urbel.task03.service.impl.UserServiceImpl;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        RoleService roleService = new RoleServiceImpl();
        OrderStatusService statusService = new OrderStatusServiceImpl();
        UserService userService = new UserServiceImpl();
        try {
            roleService.initializeRoles();
            statusService.initializeStatuses();
            userService.createInitialAdmin();
        } catch (ServiceException e) {
            LOGGER.fatal(e.getMessage(), e);
            throw new ListenerException(e);
        }
    }
}
