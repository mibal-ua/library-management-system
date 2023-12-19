package ua.mibal.minervaTest.frameworks.orm.model;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.orm.model.exception.DaoException;
import ua.mibal.minervaTest.model.Entity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
@Component
public class EntityManager {

    private final Map<Class<?>, EntityMetadata> metadataMap = new HashMap<>();
    private final DataSource dataSource;

    public EntityManager(DataSource dataSource) {
        this.dataSource = dataSource;
        initEntitiesMetadata();
    }

    public <T extends Entity> boolean save(T entity) {
        return false;
    }

    public <T extends Entity> List<T> findAll(Class<T> entityClazz) {
        return null;
    }

    public <T extends Entity> T findById(Long id, Class<T> entityClazz) {
        return null;
    }

    public <T extends Entity> boolean delete(T entity) {
        return false;
    }

    private ResultSet perform(String sql, boolean readOnly) {
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
            return perform(sql, false)
                    .rowInserted();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean deleteRow(String sql) {
        try {
            return perform(sql, false)
                    .rowDeleted();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initEntitiesMetadata() {

    }
}
