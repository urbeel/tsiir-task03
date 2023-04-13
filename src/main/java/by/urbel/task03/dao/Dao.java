package by.urbel.task03.dao;

import by.urbel.task03.dao.exception.DaoException;

import java.util.List;

public interface Dao<T, I> {
    void create(T t) throws DaoException;

    T readById(I id) throws DaoException;

    List<T> readAll() throws DaoException;

    void update(T t) throws DaoException;

    void delete(I id) throws DaoException;
}
