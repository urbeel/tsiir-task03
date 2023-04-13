package by.urbel.task03.service.impl;

import by.urbel.task03.dao.RoleDao;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.dao.impl.RoleDaoImpl;
import by.urbel.task03.entity.enums.Role;
import by.urbel.task03.service.RoleService;
import by.urbel.task03.service.exception.ServiceException;

public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao = new RoleDaoImpl();

    @Override
    public void initializeRoles() throws ServiceException {
        for (Role role : Role.values()) {
            try {
                if (roleDao.readRoleIdByName(role.name()) == null) {
                    roleDao.create(role);
                }
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage(), e);
            }
        }
    }
}
