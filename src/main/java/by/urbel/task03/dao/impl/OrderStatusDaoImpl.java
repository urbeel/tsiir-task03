package by.urbel.task03.dao.impl;

import by.urbel.task03.dao.OrderStatusDao;
import by.urbel.task03.dao.connection.ConnectionProvider;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.entity.OrderStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusDaoImpl implements OrderStatusDao {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CREATE_STATUS_QUERY =
            "INSERT INTO order_statuses (name) VALUES (?)";
    private static final String READ_STATUS_BY_ID_QUERY =
            "SELECT id, name FROM order_statuses " +
                    "WHERE id=?";
    private static final String READ_STATUS_BY_NAME_QUERY =
            "SELECT id, name FROM order_statuses " +
                    "WHERE name=?";
    private static final String READ_ALL_STATUSES_QUERY =
            "SELECT id, name FROM order_statuses";
    private static final String UPDATE_STATUS_QUERY =
            "UPDATE order_statuses SET name=? " +
                    "WHERE id=?";
    private static final String DELETE_STATUS_QUERY =
            "DELETE FROM order_statuses WHERE id=?";

    private static final String ID_NOT_NULL_MESSAGE = "Order status id cannot be null";

    @Override
    public void create(OrderStatus orderStatus) throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_STATUS_QUERY)) {
            ps.setString(1, orderStatus.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while creating order status.");
        }
    }

    @Override
    public OrderStatus readById(Long id) throws DaoException {
        if (id == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_STATUS_BY_ID_QUERY)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return resultSetToOrderStatus(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading order status by id.");
        }
    }

    @Override
    public List<OrderStatus> readAll() throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_ALL_STATUSES_QUERY)) {
            List<OrderStatus> statuses = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    statuses.add(resultSetToOrderStatus(rs));
                }
            }
            return statuses;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading all order statuses.");
        }
    }

    @Override
    public void update(OrderStatus orderStatus) throws DaoException {
        if (orderStatus.getId() == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_STATUS_QUERY)) {
            ps.setString(1, orderStatus.getName());
            ps.setLong(2, orderStatus.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while updating order status.");
        }
    }

    @Override
    public void delete(Long id) throws DaoException {
        if (id == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_STATUS_QUERY)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while deleting order status.");
        }
    }

    @Override
    public OrderStatus readByStatusName(String statusName) throws DaoException {
        if (statusName == null) {
            throw new DaoException("Status name cannot be null");
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_STATUS_BY_NAME_QUERY)) {
            ps.setString(1, statusName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return resultSetToOrderStatus(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading order status by status name.");
        }
    }

    private OrderStatus resultSetToOrderStatus(ResultSet rs) throws SQLException {
        OrderStatus status = new OrderStatus();
        status.setId(rs.getLong("id"));
        status.setName(rs.getString("name"));
        return status;
    }
}
