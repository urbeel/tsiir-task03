package by.urbel.task03.dao.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import by.urbel.task03.dao.RoleDao;
import by.urbel.task03.dao.UserDao;
import by.urbel.task03.dao.connection.ConnectionProvider;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.dao.exception.EmailAlreadyExistsException;
import by.urbel.task03.dao.exception.SQLExceptionUtil;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private static final Logger LOGGER = LogManager.getLogger();
    private final RoleDao roleDao = new RoleDaoImpl();

    private static final String CREATE_USER_QUERY =
            "INSERT INTO users(first_name,last_name,email,password,role_id,phone,address) VALUES (?,?,?,?,?,?,?)";
    private static final String SELECT_FIELDS =
            "SELECT users.id,first_name,last_name,email,password," +
                    "roles.name AS role_name,phone,address, carts.id AS cart_id FROM users " +
                    "JOIN roles ON users.role_id=roles.id " +
                    "LEFT JOIN carts ON users.id=carts.user_id ";
    private static final String READ_USER_BY_ID_QUERY =
            SELECT_FIELDS + "WHERE users.id=?";
    private static final String READ_ALL_USERS_QUERY =
            SELECT_FIELDS + "ORDER BY users.id";
    private static final String READ_USER_BY_EMAIL_QUERY =
            SELECT_FIELDS + "WHERE users.email=?";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET first_name=?,last_name=?,email=?,password=?,role_id=?,phone=?,address=? " +
                    "WHERE users.id=?";
    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE users.id=?";
    private static final String CREATE_CART_QUERY =
            "INSERT INTO carts(user_id,total_price) VALUES (?,?)";
    private static final String DELETE_CART_BY_USER_ID_QUERY =
            "DELETE FROM carts WHERE user_id=?";
    private static final String ID_NOT_NULL_MESSAGE = "User id cannot be null";

    @Override
    public void create(User user) throws DaoException {
        Long roleId = roleDao.readRoleIdByName(user.getRole().name());
        if (roleId == null) {
            throw new DaoException(String.format("Cannot find role %s", user.getRole().name()));
        }
        Connection connection = null;
        try {
            connection = ConnectionProvider.getConnection();
            try (PreparedStatement psInsertUser = connection.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psInsertCart = connection.prepareStatement(CREATE_CART_QUERY)) {
                connection.setAutoCommit(false);
                setStatementParameters(psInsertUser, user, roleId);
                psInsertUser.executeUpdate();
                try (ResultSet resultSet = psInsertUser.getGeneratedKeys()) {
                    if (resultSet.next() && user.getRole().equals(Role.CUSTOMER)) {
                        long userId = resultSet.getLong("id");
                        psInsertCart.setLong(1, userId);
                        psInsertCart.setLong(2, 0L);
                        psInsertCart.executeUpdate();
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
                    throw new DaoException("Error while creating user.");
                }
            }
            if (SQLExceptionUtil.isConstraintViolation(e)) {
                throw new EmailAlreadyExistsException("The email already exists.");
            } else {
                LOGGER.error(e.getMessage());
                throw new DaoException("Error while creating user.");
            }
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
    public User readById(Long id) throws DaoException {
        if (id == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_USER_BY_ID_QUERY)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return resultSetToUser(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading user by id.");
        }
    }

    @Override
    public List<User> readAll() throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_ALL_USERS_QUERY)) {
            List<User> users = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(resultSetToUser(rs));
                }
            }
            return users;
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading all users.");
        }
    }

    @Override
    public void update(User user) throws DaoException {
        if (user.getId() == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        Long roleId = roleDao.readRoleIdByName(user.getRole().name());
        if (roleId == null) {
            throw new DaoException(String.format("Cannot find role %s", user.getRole().name()));
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_USER_QUERY)) {
            setStatementParameters(ps, user, roleId);
            ps.setLong(8, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while updating user.");
        }
    }

    @Override
    public void delete(Long id) throws DaoException {
        if (id == null) {
            throw new DaoException(ID_NOT_NULL_MESSAGE);
        }
        Connection connection = null;
        try {
            connection = ConnectionProvider.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement psDeleteCart = connection.prepareStatement(DELETE_CART_BY_USER_ID_QUERY);
                 PreparedStatement psDeleteUser = connection.prepareStatement(DELETE_USER_QUERY)) {
                psDeleteCart.setLong(1, id);
                psDeleteCart.executeUpdate();
                psDeleteUser.setLong(1, id);
                psDeleteUser.executeUpdate();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.error(ex.getMessage());
                    throw new DaoException("Error while deleting user.");
                }
            }
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while deleting user.");
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
    public User readByEmail(String email) throws DaoException {
        if (email == null || email.isEmpty()) {
            throw new DaoException("User email cannot be null or empty");
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_USER_BY_EMAIL_QUERY)
        ) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return resultSetToUser(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading user by email.");
        }
    }

    private User resultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFirstname(rs.getString("first_name"));
        user.setLastname(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(Role.valueOf(rs.getString("role_name")));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        if (user.getRole().equals(Role.CUSTOMER)) {
            user.setCartId(rs.getLong("cart_id"));
        }
        return user;
    }

    private void setStatementParameters(PreparedStatement ps, User user, Long roleId) throws SQLException {
        ps.setString(1, user.getFirstname());
        ps.setString(2, user.getLastname());
        ps.setString(3, user.getEmail());
        ps.setString(4, BCrypt.withDefaults().hashToString(10, user.getPassword().toCharArray()));
        ps.setLong(5, roleId);
        ps.setString(6, user.getPhone());
        ps.setString(7, user.getAddress());
    }
}
