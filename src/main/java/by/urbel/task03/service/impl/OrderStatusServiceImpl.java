package by.urbel.task03.service.impl;

import by.urbel.task03.dao.OrderStatusDao;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.dao.impl.OrderStatusDaoImpl;
import by.urbel.task03.entity.OrderStatus;
import by.urbel.task03.entity.enums.StatusName;
import by.urbel.task03.service.OrderStatusService;
import by.urbel.task03.service.exception.ServiceException;

public class OrderStatusServiceImpl implements OrderStatusService {
    private final OrderStatusDao statusDao = new OrderStatusDaoImpl();

    @Override
    public void initializeStatuses() throws ServiceException {
        for (StatusName statusName : StatusName.values()) {
            try {
                if (statusDao.readByStatusName(statusName.getName()) == null) {
                    statusDao.create(new OrderStatus(null, statusName.getName()));
                }
            } catch (DaoException e) {
                throw new ServiceException(e.getMessage(), e);
            }
        }
    }
}
