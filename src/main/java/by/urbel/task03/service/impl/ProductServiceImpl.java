package by.urbel.task03.service.impl;

import by.urbel.task03.EnvVariablesNames;
import by.urbel.task03.dao.ProductDao;
import by.urbel.task03.dao.exception.DaoException;
import by.urbel.task03.dao.impl.ProductDaoImpl;
import by.urbel.task03.entity.Product;
import by.urbel.task03.service.ProductService;
import by.urbel.task03.service.exception.ServiceException;
import com.uploadcare.api.Client;
import com.uploadcare.api.File;
import com.uploadcare.upload.FileUploader;
import com.uploadcare.upload.UploadFailureException;
import com.uploadcare.upload.Uploader;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao = new ProductDaoImpl();

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String DEFAULT_PHOTO = "https://ucarecdn.com/b0157e45-fe2f-4df1-9237-5156893d37c2/";
    private static final String REGEX_TO_GET_PHOTO_ID = "https://ucarecdn.com/(.+)/.";

    @Override
    public List<Product> readAll() throws ServiceException {
        try {
            return productDao.readAll();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Product> readInStock() throws ServiceException {
        try {
            return productDao.readAllInStock();
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Product readById(long id) throws ServiceException {
        try {
            return productDao.readById(id);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void save(Product product, Part photoPart) throws ServiceException {
        validateProduct(product);
        try {
            if (photoPart != null && photoPart.getSize() > 0) {
                URI photoURI = uploadPhotoToStorage(photoPart);
                product.setPhotoUrl(photoURI.toASCIIString());
            } else {
                product.setPhotoUrl(DEFAULT_PHOTO);
            }
            productDao.create(product);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(long id, String photoUrl) throws ServiceException {
        try {
            productDao.delete(id);
            if (photoUrl != null && !photoUrl.isEmpty() && !photoUrl.equals(DEFAULT_PHOTO)) {
                deletePhotoFromStorage(photoUrl);
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void update(Product product) throws ServiceException {
        try {
            productDao.update(product);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void changeProductQuantity(long productId, int quantity) throws ServiceException {
        if (quantity < 0) {
            throw new ServiceException("Quantity must be >= 0");
        }
        try {
            productDao.changeQuantity(productId, quantity);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void validateProduct(Product product) throws ServiceException {
        if (product == null) {
            throw new ServiceException("Product cannot be null");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ServiceException("Product name cannot be null or empty");
        }
        if (product.getCategory() == null) {
            throw new ServiceException("Product category cannot be null");
        }
        if (product.getCategory().getId() == null) {
            throw new ServiceException("Product category id cannot be null");
        }
        if (product.getQuantity() < 0) {
            throw new ServiceException("Product quantity cannot be less than 0");
        }
        if (product.getPrice() < 0) {
            throw new ServiceException("Product price cannot be less than 0");
        }
    }

    private URI uploadPhotoToStorage(Part photoPart) throws ServiceException {
        String publicKey = System.getenv(EnvVariablesNames.PHOTO_STORAGE_PUBLIC_KEY);
        String secretKey = System.getenv(EnvVariablesNames.PHOTO_STORAGE_SECRET_KEY);
        Client client = new Client(publicKey, secretKey);
        try {
            Uploader uploader = new FileUploader(client, photoPart.getInputStream(), photoPart.getSubmittedFileName());
            File uploadedFile = uploader.upload();
            return uploadedFile.getOriginalFileUrl();
        } catch (UploadFailureException | IOException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException("Error while uploading product photo.");
        }
    }

    private void deletePhotoFromStorage(String photoUrl) throws ServiceException {
        String publicKey = System.getenv(EnvVariablesNames.PHOTO_STORAGE_PUBLIC_KEY);
        String secretKey = System.getenv(EnvVariablesNames.PHOTO_STORAGE_SECRET_KEY);
        Client client = new Client(publicKey, secretKey);
        Matcher matcher = Pattern.compile(REGEX_TO_GET_PHOTO_ID).matcher(photoUrl);
        if (matcher.find()) {
            String photoId = matcher.group(1);
            client.deleteFile(photoId);
        } else {
            throw new ServiceException("Invalid photo url. Photo not deleted.");
        }
    }
}
