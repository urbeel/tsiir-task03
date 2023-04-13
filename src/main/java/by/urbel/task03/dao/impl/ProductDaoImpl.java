package by.urbel.task03.dao.impl;

import by.urbel.task03.dao.ProductDao;
import by.urbel.task03.dao.connection.ConnectionProvider;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.entity.Product;
import by.urbel.task03.entity.ProductCategory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CREATE_PRODUCT_QUERY =
            "INSERT INTO products(name, category_id, description, price, quantity, photo_url) VALUES (?,?,?,?,?,?)";
    private static final String SELECT_FIELDS =
            "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.quantity AS product_quantity," +
                    " pc.id AS category_id, pc.name AS category_name, photo_url FROM products AS p " +
                    "JOIN product_categories pc on p.category_id = pc.id ";
    private static final String READ_PRODUCT_BY_ID_QUERY =
            SELECT_FIELDS + "WHERE p.id=?";
    private static final String READ_ALL_PRODUCTS_QUERY =
            SELECT_FIELDS + "ORDER BY p.id";
    private static final String READ_IN_STOCK_PRODUCTS_QUERY =
            SELECT_FIELDS + "WHERE p.quantity>0 ORDER BY p.id";
    private static final String UPDATE_PRODUCT_QUERY =
            "UPDATE products SET name=?, category_id=?, description=?, price=?, quantity=?, photo_url=? " +
                    "WHERE id=?";
    private static final String DELETE_PRODUCT_QUERY =
            "DELETE FROM products WHERE id=?";
    private static final String CHANGE_QUANTITY_QUERY =
            "UPDATE products SET quantity=? WHERE id=?";

    private static final String ID_NOT_NULL_MESSAGE = "Product id cannot be null";

    @Override
    public void create(Product product) throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_PRODUCT_QUERY)) {
            setStatementParameters(ps, product);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while creating product.");
        }
    }

    @Override
    public Product readById(Long id) throws DaoException {
        if (id == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_PRODUCT_BY_ID_QUERY)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return resultSetToProduct(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading product by id.");
        }
    }

    @Override
    public List<Product> readAll() throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_ALL_PRODUCTS_QUERY)) {
            List<Product> products = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(resultSetToProduct(rs));
                }
            }
            return products;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading all products.");
        }
    }

    @Override
    public void update(Product product) throws DaoException {
        if (product.getId() == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_PRODUCT_QUERY)) {
            setStatementParameters(ps, product);
            ps.setLong(7, product.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while updating product.");
        }
    }

    @Override
    public void delete(Long id) throws DaoException {
        if (id == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_PRODUCT_QUERY)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while deleting product.");
        }
    }

    @Override
    public List<Product> readAllInStock() throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_IN_STOCK_PRODUCTS_QUERY)) {
            List<Product> products = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(resultSetToProduct(rs));
                }
            }
            return products;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading in stock products.");
        }
    }

    @Override
    public void changeQuantity(long productId, int quantity) throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(CHANGE_QUANTITY_QUERY)) {
            ps.setInt(1, quantity);
            ps.setLong(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while changing product quantity.");
        }
    }

    public static Product resultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setCategory(new ProductCategory());
        product.setId(rs.getLong("product_id"));
        product.setName(rs.getString("product_name"));
        String description = rs.getString("description");
        product.setDescription(description != null ? description.replace("\r\n", "<br>") : null);
        product.getCategory().setId(rs.getLong("category_id"));
        product.getCategory().setName(rs.getString("category_name"));
        product.setPrice(rs.getLong("price"));
        product.setQuantity(rs.getShort("product_quantity"));
        product.setPhotoUrl(rs.getString("photo_url"));
        return product;
    }

    private void setStatementParameters(PreparedStatement ps, Product product) throws SQLException {
        ps.setString(1, product.getName());
        ps.setLong(2, product.getCategory().getId());
        ps.setString(3, product.getDescription());
        ps.setLong(4, product.getPrice());
        ps.setInt(5, product.getQuantity());
        ps.setString(6, product.getPhotoUrl());
    }
}
