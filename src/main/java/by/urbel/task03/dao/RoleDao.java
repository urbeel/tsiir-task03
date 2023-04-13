package by.urbel.task03.dao;

import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.entity.enums.Role;

public interface RoleDao {
    void create(Role role) throws DaoException;

    Long readRoleIdByName(String name) throws DaoException;
}
