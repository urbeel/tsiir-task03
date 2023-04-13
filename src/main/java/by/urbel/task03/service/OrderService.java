package by.urbel.task03.service;

import by.urbel.task03.entity.Order;
import by.urbel.task03.entity.enums.StatusName;
import by.urbel.task03.service.exception.CartNeedUpdateException;
import by.urbel.task03.service.exception.ServiceException;

import java.util.List;

public interface OrderService {
    void create(Order order) throws ServiceException, CartNeedUpdateException;

    List<Order> readOrdersByStatusName(StatusName statusName) throws ServiceException;

    void changeOrderStatus(long orderId, StatusName statusName) throws ServiceException;
}
