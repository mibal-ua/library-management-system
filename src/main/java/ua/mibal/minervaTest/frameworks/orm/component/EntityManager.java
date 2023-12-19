package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.context.component.FileLoader;
import ua.mibal.minervaTest.frameworks.orm.model.EntityMetadata;
import ua.mibal.minervaTest.frameworks.orm.model.exception.DaoException;
import ua.mibal.minervaTest.model.Entity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    private final SqlRequestGenerator sqlRequestGenerator;
    private Map<Class<?>, EntityMetadata> metadataMap;

    public EntityManager(DataSource dataSource,
                         String entityPackage,
                         SqlRequestGenerator sqlRequestGenerator) {
        this.dataSource = dataSource;
        this.sqlRequestGenerator = sqlRequestGenerator;
        initEntitiesMetadata(entityPackage);
    }

    public <T extends Entity> boolean save(T entity) {
        EntityMetadata metadata = metadataMap.get(entity.getClass());
        String sql = sqlRequestGenerator.save(metadata);
        return perform(statement -> {
            insertFields(statement, entity, metadata);
            return statement.executeUpdate() == 1;
        }, sql, false);
    }

    private <T extends Entity> void insertFields(PreparedStatement preparedStatement,
                                                 T entity,
                                                 EntityMetadata metadata) {
        // TODO
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

    private <R> R perform(ExceptionAllowsFunction<PreparedStatement, R> statementFunction,
                          String sql,
                          boolean readOnly) {
        try (Connection connection = dataSource.getConnection()) {
            connection.beginRequest();
            connection.setReadOnly(readOnly);

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            R result = statementFunction.apply(preparedStatement);

            connection.commit();
            return result;
        } catch (SQLException e) {
            throw new DaoException(e);
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

    @FunctionalInterface
    public interface ExceptionAllowsFunction<T, R> {
        R apply(T value) throws SQLException;
    }
}
