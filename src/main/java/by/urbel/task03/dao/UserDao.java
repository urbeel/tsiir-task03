package by.urbel.task03.dao;

import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.entity.User;

public interface UserDao extends Dao<User, Long> {
    User readByEmail(String email) throws DaoException;
}
