package by.urbel.task03.dao.impl;

import by.urbel.task03.dao.OrderDao;
import by.urbel.task03.dao.connection.ConnectionProvider;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.entity.Item;
import by.urbel.task03.entity.Order;
import by.urbel.task03.entity.OrderStatus;
import by.urbel.task03.entity.User;
import by.urbel.task03.entity.enums.StatusName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CREATE_ORDER_QUERY =
            "INSERT INTO orders (order_status_id, user_id, order_time, total_price) VALUES (?,?,?,?)";
    private static final String CREATE_ORDER_ITEM_QUERY =
            "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?,?,?)";
    private static final String UPDATE_PRODUCT_QUANTITY_QUERY =
            "UPDATE products SET quantity=quantity-? WHERE id=?";
    private static final String CLEAR_CART_QUERY =
            "DELETE FROM cart_items as ci " +
                    "WHERE ci.cart_id IN (SELECT c.id FROM carts as c WHERE c.user_id=?)";
    private static final String UPDATE_CART_PRICE_QUERY =
            "UPDATE carts SET total_price=0 " +
                    "WHERE user_id=?";
    private static final String READ_ORDERS_BY_STATUS_QUERY =
            "SELECT orders.id AS order_id, order_time, total_price, os.id AS status_id, os.name AS status_name, " +
                    "user_id, first_name, last_name, email, phone, address FROM orders " +
                    "JOIN order_statuses os on orders.order_status_id = os.id " +
                    "JOIN users u on orders.user_id = u.id " +
                    "WHERE os.name=?";
    private static final String READ_ORDER_ITEMS_QUERY =
            "SELECT oi.order_id, oi.product_id, oi.quantity AS order_item_quantity, p.name AS product_name, " +
                    "p.category_id, p.description, p.price, p.quantity AS product_quantity, photo_url, " +
                    "pc.name AS category_name  FROM order_items AS oi " +
                    "JOIN products AS p ON oi.product_id = p.id " +
                    "JOIN product_categories AS pc ON p.category_id = pc.id " +
                    "WHERE order_id=?";
    private static final String UPDATE_ORDER_QUERY =
            "UPDATE orders SET order_status_id=?, user_id=?, order_time=?, total_price=?" +
                    "WHERE id=?";
    private static final String DELETE_ORDER_QUERY =
            "DELETE FROM orders WHERE id=?";
    private static final String UPDATE_STATUS_NAME_QUERY =
            "UPDATE orders SET order_status_id=(SELECT id FROM order_statuses WHERE name=?) WHERE orders.id=?";
    private static final String ADD_TO_PRODUCT_QUANTITY_QUERY =
            "UPDATE products SET quantity=quantity+? WHERE id=?";

    @Override
    public void create(Order order) throws DaoException {
        Connection connection = null;
        try {
            connection = ConnectionProvider.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement psInsertOrder = connection.prepareStatement(CREATE_ORDER_QUERY, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psInsertItems = connection.prepareStatement(CREATE_ORDER_ITEM_QUERY);
                 PreparedStatement psUpdateProductQuantity = connection.prepareStatement(UPDATE_PRODUCT_QUANTITY_QUERY);
                 PreparedStatement psClearCart = connection.prepareStatement(CLEAR_CART_QUERY);
                 PreparedStatement psUpdateCartPrice = connection.prepareStatement(UPDATE_CART_PRICE_QUERY)) {
                setOrderStatementParameters(psInsertOrder, order);
                psInsertOrder.executeUpdate();
                try (ResultSet resultSet = psInsertOrder.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        long orderId = resultSet.getLong("id");
                        for (Item item : order.getItems()) {
                            psInsertItems.setLong(1, orderId);
                            psInsertItems.setLong(2, item.getProduct().getId());
                            psInsertItems.setLong(3, item.getQuantity());
                            psInsertItems.executeUpdate();
                            psUpdateProductQuantity.setInt(1, item.getQuantity());
                            psUpdateProductQuantity.setLong(2, item.getProduct().getId());
                            psUpdateProductQuantity.executeUpdate();
                            psClearCart.setLong(1, order.getUser().getId());
                            psClearCart.executeUpdate();
                            psUpdateCartPrice.setLong(1, order.getUser().getId());
                            psUpdateCartPrice.executeUpdate();
                        }
                        connection.commit();
                    } else {
                        connection.rollback();
                        throw new DaoException("Order not created.");
                    }
                }
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                    throw new DaoException("Error while creating order.");
                }
            }
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while creating order.");
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                }
            }
        }
    }

    @Override
    public void update(Order order) throws DaoException {
        if (order.getId() == null) {
            throw new DaoException("Order id cannot be null");
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_ORDER_QUERY)) {
            setOrderStatementParameters(ps, order);
            ps.setLong(5, order.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while updating order.");
        }
    }

    @Override
    public void delete(long id) throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_ORDER_QUERY)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while deleting order.");
        }
    }

    @Override
    public List<Order> readOrdersByStatusName(String statusName) throws DaoException {
        if (statusName == null || statusName.trim().isEmpty()) {
            throw new DaoException("Order status name cannot be null or empty.");
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement psOrders = connection.prepareStatement(READ_ORDERS_BY_STATUS_QUERY);
             PreparedStatement psItems = connection.prepareStatement(READ_ORDER_ITEMS_QUERY)) {
            psOrders.setString(1, statusName);
            try (ResultSet rsOrders = psOrders.executeQuery()) {
                List<Order> orders = new ArrayList<>();
                while (rsOrders.next()) {
                    Order order = resultSetToOrder(rsOrders);
                    psItems.setLong(1, order.getId());
                    try (ResultSet rsItems = psItems.executeQuery()) {
                        order.setItems(new ArrayList<>());
                        while (rsItems.next()) {
                            order.getItems().add(resultSetToItem(rsItems));
                        }
                        orders.add(order);
                    }
                }
                return orders;
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading orders by status name.");
        }
    }

    @Override
    public void changeOrderStatus(long orderId, String statusName) throws DaoException {
        if (statusName == null || statusName.trim().isEmpty()) {
            throw new DaoException("Order status name cannot be null or empty.");
        }
        Connection connection = null;
        try {
            connection = ConnectionProvider.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement psUpdateStatus = connection.prepareStatement(UPDATE_STATUS_NAME_QUERY)) {
                psUpdateStatus.setString(1, statusName);
                psUpdateStatus.setLong(2, orderId);
                psUpdateStatus.executeUpdate();
                if (statusName.equals(StatusName.CANCELED.getName())) {
                    try (PreparedStatement psItems = connection.prepareStatement(READ_ORDER_ITEMS_QUERY);
                         PreparedStatement psUpdateProductQuantity = connection.prepareStatement(ADD_TO_PRODUCT_QUANTITY_QUERY)) {
                        psItems.setLong(1, orderId);
                        try (ResultSet rsItems = psItems.executeQuery()) {
                            while (rsItems.next()) {
                                Item item = resultSetToItem(rsItems);
                                psUpdateProductQuantity.setInt(1, item.getQuantity());
                                psUpdateProductQuantity.setLong(2, item.getProduct().getId());
                                psUpdateProductQuantity.executeUpdate();
                            }
                        }
                    }
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                    throw new DaoException("Error while changing order status.");
                }
            }
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while changing order status.");
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

    private void setOrderStatementParameters(PreparedStatement ps, Order order) throws SQLException {
        ps.setLong(1, order.getStatus().getId());
        ps.setLong(2, order.getUser().getId());
        ps.setTimestamp(3, order.getOrderTime());
        ps.setLong(4, order.getTotalPrice());
    }

    private Order resultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setUser(new User());
        order.setStatus(new OrderStatus());
        order.setId(rs.getLong("order_id"));
        order.setOrderTime(rs.getTimestamp("order_time"));
        order.setTotalPrice(rs.getLong("total_price"));
        order.getUser().setId(rs.getLong("user_id"));
        order.getUser().setEmail(rs.getString("email"));
        order.getUser().setFirstname(rs.getString("first_name"));
        order.getUser().setLastname(rs.getString("last_name"));
        order.getUser().setPhone(rs.getString("phone"));
        order.getUser().setAddress(rs.getString("address"));
        order.getStatus().setId(rs.getLong("status_id"));
        order.getStatus().setName(rs.getString("status_name"));
        return order;
    }

    private Item resultSetToItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setQuantity(rs.getInt("order_item_quantity"));
        item.setProduct(ProductDaoImpl.resultSetToProduct(rs));
        return item;
    }
}
