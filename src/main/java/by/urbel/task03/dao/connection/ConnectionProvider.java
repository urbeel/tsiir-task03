package by.urbel.task03.dao.connection;

import by.urbel.task03.EnvVariablesNames;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionProvider {
    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setUrl(System.getenv(EnvVariablesNames.DB_URL));
        dataSource.setUsername(System.getenv(EnvVariablesNames.DB_USERNAME));
        dataSource.setPassword(System.getenv(EnvVariablesNames.DB_PASSWORD));
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.addConnectionProperty("useUnicode", "true");
        dataSource.addConnectionProperty("characterEncoding", "UTF8");
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
