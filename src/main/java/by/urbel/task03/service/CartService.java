package by.urbel.task03.service;

import by.urbel.task03.entity.Cart;
import by.urbel.task03.entity.Item;
import by.urbel.task03.service.exception.ServiceException;

public interface CartService {
    void addProductToCart(Item item, long cartId) throws ServiceException;

    void removeProductFromCart(Item item, long cartId) throws ServiceException;

    Cart readById(long cartId) throws ServiceException;
}
