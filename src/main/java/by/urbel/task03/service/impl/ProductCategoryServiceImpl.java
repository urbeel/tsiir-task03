package by.urbel.task03.service.impl;

import by.urbel.task03.dao.ProductCategoryDao;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.dao.impl.ProductCategoryDaoImpl;
import by.urbel.task03.entity.ProductCategory;
import by.urbel.task03.service.ProductCategoryService;
import by.urbel.task03.service.exception.ServiceException;

import java.util.List;
import java.util.stream.Collectors;

public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryDao categoryDao = new ProductCategoryDaoImpl();

    @Override
    public void create(ProductCategory category) throws ServiceException {
        validateCategory(category);
        try {
            categoryDao.create(category);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<ProductCategory> readAll() throws ServiceException {
        try {
            return categoryDao.readAll().stream().sorted().collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void update(ProductCategory category) throws ServiceException {
        validateCategory(category);
        try {
            categoryDao.update(category);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            categoryDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void validateCategory(ProductCategory category) throws ServiceException {
        if (category == null) {
            throw new ServiceException("Product category cannot be null");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new ServiceException("Product category name cannot be null or empty");
        }
    }
}
