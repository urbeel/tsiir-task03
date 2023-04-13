package by.urbel.task03.service;

import by.urbel.task03.entity.ProductCategory;
import by.urbel.task03.service.exception.ServiceException;

import java.util.List;

public interface ProductCategoryService {
    void create(ProductCategory category) throws ServiceException;

    List<ProductCategory> readAll() throws ServiceException;

    void update(ProductCategory category) throws ServiceException;

    void delete(long id) throws ServiceException;

}
