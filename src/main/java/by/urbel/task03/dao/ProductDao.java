package by.urbel.task03.dao;

import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.entity.Product;

import java.util.List;

public interface ProductDao extends Dao<Product, Long> {
    List<Product> readAllInStock() throws DaoException;

    void changeQuantity(long productId, int quantity) throws DaoException;
}
