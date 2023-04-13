package by.urbel.task03.dao.impl;

import by.urbel.task03.dao.ProductCategoryDao;
import by.urbel.task03.dao.connection.ConnectionProvider;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.dao.exception.SQLExceptionUtil;
import by.urbel.task03.entity.ProductCategory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoImpl implements ProductCategoryDao {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CREATE_CATEGORY_QUERY =
            "INSERT INTO product_categories (name) VALUES (?)";
    private static final String READ_CATEGORY_BY_ID_QUERY =
            "SELECT pc.id, pc.name FROM product_categories AS pc " +
                    "WHERE pc.id=?";
    private static final String READ_ALL_CATEGORIES_QUERY =
            "SELECT pc.id, pc.name FROM product_categories AS pc";
    private static final String UPDATE_CATEGORY_QUERY =
            "UPDATE product_categories SET name=? " +
                    "WHERE id=?";
    private static final String DELETE_CATEGORY_QUERY =
            "DELETE FROM product_categories WHERE id=?";

    private static final String ID_NOT_NULL_MESSAGE = "Product_category id cannot be null";

    @Override
    public void create(ProductCategory category) throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_CATEGORY_QUERY)) {
            ps.setString(1, category.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            if (SQLExceptionUtil.isConstraintViolation(e)) {
                throw new DaoException("Category with this name already exists.");
            }
            throw new DaoException("Error while creating product category.");
        }
    }

    @Override
    public ProductCategory readById(Long id) throws DaoException {
        if (id == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_CATEGORY_BY_ID_QUERY)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return resultSetToProductCategory(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading product category by id.");
        }
    }

    @Override
    public List<ProductCategory> readAll() throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_ALL_CATEGORIES_QUERY)) {
            List<ProductCategory> categories = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(resultSetToProductCategory(rs));
                }
            }
            return categories;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading all product categories.");
        }
    }

    @Override
    public void update(ProductCategory category) throws DaoException {
        if (category.getId() == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_CATEGORY_QUERY)) {
            ps.setString(1, category.getName());
            ps.setLong(2, category.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            if (SQLExceptionUtil.isConstraintViolation(e)) {
                throw new DaoException("Category with this name already exists.");
            }
            throw new DaoException("Error while updating product category.");
        }
    }

    @Override
    public void delete(Long id) throws DaoException {
        if (id == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_CATEGORY_QUERY)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while deleting product category.");
        }
    }

    private ProductCategory resultSetToProductCategory(ResultSet rs) throws SQLException {
        ProductCategory category = new ProductCategory();
        category.setId(rs.getLong("id"));
        category.setName(rs.getString("name"));
        return category;
    }
}
