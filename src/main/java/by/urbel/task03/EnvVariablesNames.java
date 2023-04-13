package by.urbel.task03;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnvVariablesNames {
    public static final String DB_URL = "DB_URL";
    public static final String DB_USERNAME = "DB_USERNAME";
    public static final String DB_PASSWORD = "DB_PASSWORD";
    public static final String ADMIN_EMAIL = "ADMIN_EMAIL";
    public static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";
    public static final String ADMIN_FIRSTNAME = "ADMIN_FIRSTNAME";
    public static final String ADMIN_LASTNAME = "ADMIN_LASTNAME";
    public static final String PHOTO_STORAGE_PUBLIC_KEY = "UPLOADCARE_PUBLIC_KEY";
    public static final String PHOTO_STORAGE_SECRET_KEY = "UPLOADCARE_SECRET_KEY";
}
