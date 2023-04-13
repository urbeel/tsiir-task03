package by.urbel.task03.service;

import by.urbel.task03.entity.User;
import by.urbel.task03.service.exception.BadCredentialsException;
import by.urbel.task03.service.exception.ServiceException;

import java.util.List;

public interface UserService {
    void register(User user) throws ServiceException;

    void createInitialAdmin() throws ServiceException;

    User authorize(String email, String password) throws ServiceException, BadCredentialsException;

    List<User> readAll() throws ServiceException;

    void delete(long id) throws ServiceException;
}
