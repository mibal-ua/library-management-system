package ua.mibal.minervaTest.frameworks.orm;

import ua.mibal.minervaTest.frameworks.orm.component.ResultInterpreter;
import ua.mibal.minervaTest.frameworks.orm.component.SqlRequestGenerator;
import ua.mibal.minervaTest.frameworks.orm.model.exception.DaoException;
import ua.mibal.minervaTest.model.Entity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public abstract class Repository<T extends Entity> {

    private final DataSource dataSource;
    private final SqlRequestGenerator<T> sqlRequestGenerator;
    private final ResultInterpreter<T> resultInterpreter;

    protected Repository(Class<T> entityClazz, DataSource dataSource) {
        this.dataSource = dataSource;
        this.sqlRequestGenerator = new SqlRequestGenerator<>(entityClazz);
        this.resultInterpreter = new ResultInterpreter<>(entityClazz);
    }

    public Optional<T> findById(Long id) {
        String sql = sqlRequestGenerator.findById(id);
        ResultSet resultSet = get(sql, true);
        return resultInterpreter.interpret(resultSet);
    }

    public List<T> findAll() {
        String sql = sqlRequestGenerator.findAll();
        ResultSet resultSet = get(sql, true);
        return resultInterpreter.interpretList(resultSet);
    }

    public boolean save(T entity) {
        String sql = sqlRequestGenerator.save(entity);
        return insert(sql);
    }

    public boolean delete(T entity) {
        String sql = sqlRequestGenerator.delete(entity);
        return deleteRow(sql);
    }

    private ResultSet get(String sql, boolean readOnly) {
        try (Connection connection = dataSource.getConnection()) {
            connection.beginRequest();
            connection.setReadOnly(readOnly);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            connection.commit();
            return resultSet;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private boolean insert(String sql) {
        try {
            return get(sql, false)
                    .rowInserted();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean deleteRow(String sql) {
        try {
            return get(sql, false)
                    .rowDeleted();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
