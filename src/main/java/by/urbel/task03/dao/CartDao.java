package by.urbel.task03.dao;

import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.entity.Cart;
import by.urbel.task03.entity.Item;

public interface CartDao {
    void create(Cart cart) throws DaoException;

    Cart readById(long id) throws DaoException;

    void delete(long id) throws DaoException;

    void addItemToCart(long cartId, Item item) throws DaoException;

    void removeItemFromCart(long cartId, Item item) throws DaoException;
}
