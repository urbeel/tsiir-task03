package by.urbel.task03.dao;

import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.entity.OrderStatus;

public interface OrderStatusDao extends Dao<OrderStatus, Long> {
    OrderStatus readByStatusName(String statusName) throws DaoException;
}
