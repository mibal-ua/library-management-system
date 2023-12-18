package ua.mibal.minervaTest.frameworks.orm.model.exception;

import java.sql.SQLException;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class DaoException extends RuntimeException {

    public DaoException(SQLException e) {
        super(e);
    }
}
