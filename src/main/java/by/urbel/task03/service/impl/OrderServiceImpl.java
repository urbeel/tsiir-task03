package by.urbel.task03.service.impl;

import by.urbel.task03.dao.CartDao;
import by.urbel.task03.dao.OrderDao;
import by.urbel.task03.dao.OrderStatusDao;
import by.urbel.task03.dao.ProductDao;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.dao.impl.CartDaoImpl;
import by.urbel.task03.dao.impl.OrderDaoImpl;
import by.urbel.task03.dao.impl.OrderStatusDaoImpl;
import by.urbel.task03.dao.impl.ProductDaoImpl;
import by.urbel.task03.entity.Item;
import by.urbel.task03.entity.Order;
import by.urbel.task03.entity.OrderStatus;
import by.urbel.task03.entity.Product;
import by.urbel.task03.entity.enums.StatusName;
import by.urbel.task03.service.OrderService;
import by.urbel.task03.service.exception.CartNeedUpdateException;
import by.urbel.task03.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao = new OrderDaoImpl();
    private final OrderStatusDao statusDao = new OrderStatusDaoImpl();
    private final ProductDao productDao = new ProductDaoImpl();
    private final CartDao cartDao = new CartDaoImpl();
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void create(Order order) throws ServiceException, CartNeedUpdateException {
        try {
            validateOrder(order);
            OrderStatus status = statusDao.readByStatusName(StatusName.NEW.getName());
            if (status == null) {
                LOGGER.error("Order not created because status {} not found.", StatusName.NEW.getName());
                throw new ServiceException("Error while creating order.");
            }
            order.setStatus(status);
            order.setOrderTime(Timestamp.valueOf(LocalDateTime.now()));
            boolean isItemsUpdated = false;
            for (Item item : order.getItems()) {
                Product product = productDao.readById(item.getProduct().getId());
                if (product == null) {
                    cartDao.removeItemFromCart(order.getUser().getCartId(), item);
                    isItemsUpdated = true;
                } else {
                    if (item.getQuantity() > product.getQuantity()) {
                        cartDao.removeItemFromCart(order.getUser().getCartId(), item);
                        item.setQuantity(product.getQuantity());
                        cartDao.addItemToCart(order.getUser().getCartId(), item);
                        isItemsUpdated = true;
                    }
                }
            }
            if (!isItemsUpdated) {
                orderDao.create(order);
            } else {
                throw new CartNeedUpdateException();
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Order> readOrdersByStatusName(StatusName statusName) throws ServiceException {
        if (statusName == null) {
            throw new ServiceException("Status name cannot be null.");
        }
        try {
            return orderDao.readOrdersByStatusName(statusName.getName());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void changeOrderStatus(long orderId, StatusName statusName) throws ServiceException {
        if (statusName == null) {
            throw new ServiceException("Status name cannot be null.");
        }
        try {
            orderDao.changeOrderStatus(orderId, statusName.getName());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void validateOrder(Order order) throws ServiceException {
        if (order == null) {
            throw new ServiceException("Order cannot be null");
        }
        if (order.getUser() == null || order.getUser().getId() == null) {
            throw new ServiceException("User in order cannot be null");
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new ServiceException("No products for order.");
        }
        if (order.getTotalPrice() < 0) {
            throw new ServiceException("Order total price cannot be less than 0");
        }
    }
}
