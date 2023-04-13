package by.urbel.task03.dao.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SQLExceptionUtil {
    public static boolean isConstraintViolation(SQLException e) {
        return e.getSQLState().equals("23505");
    }
}
