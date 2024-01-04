package ua.mibal.minervaTest.frameworks.orm.model;

import java.sql.PreparedStatement;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class SqlRequest<T> {

    // TODO

    private String sql;

    public String getSql() {
        return sql;
    }

    public void insertValues(PreparedStatement preparedStatement, T entity) {

    }

    public void insertValues(PreparedStatement preparedStatement, Long id) {

    }
}
