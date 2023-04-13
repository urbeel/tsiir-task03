package by.urbel.task03.dao;

import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.entity.Order;

import java.util.List;

public interface OrderDao {
    void create(Order order) throws DaoException;

    void update(Order order) throws DaoException;

    void delete(long id) throws DaoException;

    List<Order> readOrdersByStatusName(String statusName) throws DaoException;

    void changeOrderStatus(long orderId, String statusName) throws DaoException;
}
