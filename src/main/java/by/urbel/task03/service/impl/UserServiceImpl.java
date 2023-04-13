package by.urbel.task03.service.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import by.urbel.task03.EnvVariablesNames;
import by.urbel.task03.dao.UserDao;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.dao.exception.EmailAlreadyExistsException;
import by.urbel.task03.dao.impl.UserDaoImpl;
import by.urbel.task03.entity.User;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.service.UserService;
import by.urbel.task03.service.exception.BadCredentialsException;
import by.urbel.task03.service.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDaoImpl();

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void register(User user) throws ServiceException {
        validateUser(user);
        try {
            userDao.create(user);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void createInitialAdmin() throws ServiceException {
        User admin = new User();
        admin.setEmail(System.getenv(EnvVariablesNames.ADMIN_EMAIL));
        admin.setPassword(System.getenv(EnvVariablesNames.ADMIN_PASSWORD));
        admin.setFirstname(System.getenv(EnvVariablesNames.ADMIN_FIRSTNAME));
        admin.setLastname(System.getenv(EnvVariablesNames.ADMIN_LASTNAME));
        admin.setRole(Role.ADMIN);
        try {
            userDao.create(admin);
        } catch (EmailAlreadyExistsException e) {
            LOGGER.error(e);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public User authorize(String email, String password) throws ServiceException, BadCredentialsException {
        if (email == null || email.trim().isEmpty()) {
            throw new ServiceException("Email cannot be null");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ServiceException("Password cannot be null");
        }
        User user;
        try {
            user = userDao.readByEmail(email);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        if (user != null && isValidPassword(password, user.getPassword())) {
            user.setPassword(null);
            return user;
        } else {
            throw new BadCredentialsException("Incorrect password or email");
        }
    }

    @Override
    public List<User> readAll() throws ServiceException {
        try {
            List<User> users = userDao.readAll();
            users.forEach(user -> user.setPassword(null));
            return users;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            userDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private boolean isValidPassword(String password, String hash) throws ServiceException {
        if (password == null || password.trim().isEmpty()) {
            throw new ServiceException("Password cannot be null or empty");
        }
        if (hash == null || hash.trim().isEmpty()) {
            throw new ServiceException("Hash cannot be null or empty");
        }
        return BCrypt.verifyer().verify(password.toCharArray(), hash.toCharArray()).verified;
    }

    private void validateUser(User user) throws ServiceException {
        if (user == null) {
            throw new ServiceException("User cannot be null");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new ServiceException("User email cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new ServiceException("User password cannot be null or empty");
        }
        if (user.getFirstname() == null || user.getFirstname().trim().isEmpty()) {
            throw new ServiceException("User firstname cannot be null or empty");
        }
        if (user.getLastname() == null || user.getLastname().trim().isEmpty()) {
            throw new ServiceException("User lastname cannot be null or empty");
        }
        if (user.getRole() == null) {
            throw new ServiceException("User role cannot be null or empty");
        }
        if (user.getRole().equals(Role.CUSTOMER)) {
            if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
                throw new ServiceException("User phone number cannot be null or empty");
            }
            if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
                throw new ServiceException("User address cannot be null or empty");
            }
        }
    }
}
