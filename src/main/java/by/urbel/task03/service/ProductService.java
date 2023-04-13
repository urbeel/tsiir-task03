package by.urbel.task03.service;

import by.urbel.task03.entity.Product;
import by.urbel.task03.service.exception.ServiceException;
import jakarta.servlet.http.Part;

import java.util.List;

public interface ProductService {
    List<Product> readAll() throws ServiceException;

    List<Product> readInStock() throws ServiceException;

    Product readById(long id) throws ServiceException;

    void save(Product product, Part photoPart) throws ServiceException;

    void delete(long id, String photoUrl) throws ServiceException;

    void update(Product product) throws ServiceException;

    void changeProductQuantity(long productId, int quantity) throws ServiceException;
}
