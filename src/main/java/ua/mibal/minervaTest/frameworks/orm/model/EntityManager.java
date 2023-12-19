package ua.mibal.minervaTest.frameworks.orm.model;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.context.component.FileLoader;
import ua.mibal.minervaTest.frameworks.orm.component.MetadataBuilder;
import ua.mibal.minervaTest.frameworks.orm.model.exception.DaoException;
import ua.mibal.minervaTest.model.Entity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
@Component
public class EntityManager {

    private final DataSource dataSource;
    private Map<Class<?>, EntityMetadata> metadataMap;

    public EntityManager(DataSource dataSource, String entityPackage) {
        this.dataSource = dataSource;
        initEntitiesMetadata(entityPackage);
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

    private void initEntitiesMetadata(String entityPackage) {
        MetadataBuilder metadataBuilder = new MetadataBuilder();
        metadataMap = new FileLoader()
                .classesInDir(entityPackage)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(jakarta.persistence.Entity.class))
                .collect(Collectors.toMap(
                        identity(),
                        metadataBuilder::build
                ));
    }
}
