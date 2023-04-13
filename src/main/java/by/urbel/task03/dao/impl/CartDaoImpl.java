package by.urbel.task03.dao.impl;

import by.urbel.task03.dao.CartDao;
import by.urbel.task03.dao.connection.ConnectionProvider;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.entity.Cart;
import by.urbel.task03.entity.Item;
import by.urbel.task03.entity.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class CartDaoImpl implements CartDao {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CREATE_CART_QUERY =
            "INSERT INTO carts(user_id,total_price) VALUES (?,?)";
    private static final String READ_CART_BY_ID_QUERY =
            "SELECT c.id AS cart_id, user_id, c.total_price, ci.quantity AS cart_item_quantity, p.id AS product_id, p.name AS product_name, " +
                    "p.description, p.price, p.quantity AS product_quantity, pc.id AS category_id, pc.name AS category_name, photo_url FROM carts AS c " +
                    "LEFT JOIN cart_items AS ci on c.id = ci.cart_id " +
                    "LEFT JOIN products AS p on ci.product_id = p.id " +
                    "LEFT JOIN product_categories pc on p.category_id = pc.id " +
                    "WHERE c.id=?";
    private static final String UPDATE_TOTAL_PRICE_QUERY =
            "UPDATE carts SET total_price=total_price+? " +
                    "WHERE id=?";
    private static final String DELETE_CART_QUERY =
            "DELETE FROM carts WHERE id=?";
    private static final String ADD_CART_ITEM_QUERY =
            "INSERT INTO cart_items(cart_id,product_id,quantity) VALUES (?,?,?)";
    private static final String DELETE_CART_ITEM_QUERY =
            "DELETE FROM cart_items WHERE cart_id=? AND product_id=?";

    @Override
    public void create(Cart cart) throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_CART_QUERY)) {
            setStatementParameters(ps, cart);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while creating cart.");
        }
    }

    @Override
    public Cart readById(long id) throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_CART_BY_ID_QUERY)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return resultSetToCart(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading cart by id.");
        }
    }

    @Override
    public void delete(long id) throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_CART_QUERY)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while deleting cart.");
        }
    }

    @Override
    public void addItemToCart(long cartId, Item item) throws DaoException {
        Connection connection = null;
        try {
            connection = ConnectionProvider.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement psAddItem = connection.prepareStatement(ADD_CART_ITEM_QUERY);
                 PreparedStatement psUpdateTotalPrice = connection.prepareStatement(UPDATE_TOTAL_PRICE_QUERY)) {
                psAddItem.setLong(1, cartId);
                psAddItem.setLong(2, item.getProduct().getId());
                psAddItem.setInt(3, item.getQuantity());
                psAddItem.executeUpdate();
                long price = item.getQuantity() * item.getProduct().getPrice();
                psUpdateTotalPrice.setLong(1, price);
                psUpdateTotalPrice.setLong(2, cartId);
                psUpdateTotalPrice.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                    throw new DaoException("Error while adding product to cart.");
                }
            }
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while adding product to cart.");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }
        }
    }

    @Override
    public void removeItemFromCart(long cartId, Item item) throws DaoException {
        Connection connection = null;
        try {
            connection = ConnectionProvider.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement psDeleteItem = connection.prepareStatement(DELETE_CART_ITEM_QUERY);
                 PreparedStatement psUpdateTotalPrice = connection.prepareStatement(UPDATE_TOTAL_PRICE_QUERY)) {
                psDeleteItem.setLong(1, cartId);
                psDeleteItem.setLong(2, item.getProduct().getId());
                psDeleteItem.executeUpdate();
                long price = -item.getQuantity() * item.getProduct().getPrice();
                psUpdateTotalPrice.setLong(1, price);
                psUpdateTotalPrice.setLong(2, cartId);
                psUpdateTotalPrice.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                    throw new DaoException("Error while deleting product from cart.");
                }
            }
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while deleting product from cart.");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }
        }
    }

    private Cart resultSetToCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setId(rs.getLong("cart_id"));
        cart.setUserId(rs.getLong("user_id"));
        cart.setTotalPrice(rs.getLong("total_price"));
        if (rs.getString("product_name") == null) {
            cart.setItems(Collections.emptyList());
            return cart;
        }
        cart.setItems(new ArrayList<>());
        do {
            Item item = new Item();
            Product product = ProductDaoImpl.resultSetToProduct(rs);
            item.setQuantity(rs.getInt("cart_item_quantity"));
            item.setProduct(product);
            cart.getItems().add(item);
        } while (rs.next());
        return cart;
    }

    private void setStatementParameters(PreparedStatement ps, Cart cart) throws SQLException {
        ps.setLong(1, cart.getUserId());
        ps.setLong(2, cart.getTotalPrice());
    }
}
