package by.urbel.task03.service;

import by.urbel.task03.service.exception.ServiceException;

public interface OrderStatusService {
    void initializeStatuses() throws ServiceException;
}
