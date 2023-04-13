package by.urbel.task03.dao.impl;

import by.urbel.task03.dao.RoleDao;
import by.urbel.task03.dao.connection.ConnectionProvider;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.entity.enums.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDaoImpl implements RoleDao {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String CREATE_ROLE_QUERY =
            "INSERT INTO roles(name) VALUES (?)";
    private static final String READ_ROLE_ID_BY_NAME_QUERY =
            "SELECT id FROM roles WHERE name=?";

    @Override
    public void create(Role role) throws DaoException {
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(CREATE_ROLE_QUERY)) {
            ps.setString(1, role.name());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while creating role.");
        }
    }

    @Override
    public Long readRoleIdByName(String name) throws DaoException {
        if (name == null || name.trim().isEmpty()) {
            throw new DaoException("Role name cannot be null or empty.");
        }
        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement ps = connection.prepareStatement(READ_ROLE_ID_BY_NAME_QUERY)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return null;
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new DaoException("Error while reading role by name.");
        }
    }
}
