package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.context.component.FileLoader;
import ua.mibal.minervaTest.frameworks.orm.model.EntityMetadata;
import ua.mibal.minervaTest.frameworks.orm.model.SqlRequest;
import ua.mibal.minervaTest.frameworks.orm.model.exception.DaoException;
import ua.mibal.minervaTest.model.Entity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final ResultInterpreter resultInterpreter;
    private final IdProvider idProvider;
    private Map<Class<?>, EntityMetadata> metadataMap;

    public EntityManager(DataSource dataSource,
                         String entityPackage,
                         ResultInterpreter resultInterpreter,
                         SqlRequestGenerator sqlRequestGenerator) {
        this.dataSource = dataSource;
        this.sqlRequestGenerator = sqlRequestGenerator;
        initEntitiesMetadata(entityPackage);
        this.idProvider = new IdProvider(metadataMap.keySet());
        this.resultInterpreter = resultInterpreter;
    }

    public <T extends Entity> boolean save(T entity) {
        EntityMetadata metadata = metadataMap.get(entity.getClass());
        SqlRequest<T> request = sqlRequestGenerator.save(metadata, idProvider);
        return perform(conn -> {
                    PreparedStatement preparedStatement =
                            conn.prepareStatement(request.getSql());
                    request.insertValues(preparedStatement, entity);
                    return preparedStatement.executeUpdate() == 1;
                }, false
        );
    }

    public <T extends Entity> boolean delete(T entity) {
        EntityMetadata metadata = metadataMap.get(entity.getClass());
        SqlRequest<T> request = sqlRequestGenerator.delete(metadata);
        return perform(conn -> {
            PreparedStatement preparedStatement =
                    conn.prepareStatement(request.getSql());
            request.insertValues(preparedStatement, entity);
            return preparedStatement.executeQuery().rowDeleted();
        }, false);
    }


    public <T extends Entity> List<T> findAll(Class<T> entityClazz) {
        EntityMetadata metadata = metadataMap.get(entityClazz);
        SqlRequest<T> request = sqlRequestGenerator.findAll(metadata);
        return perform(conn -> {
            PreparedStatement preparedStatement =
                    conn.prepareStatement(request.getSql());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultInterpreter.interpretList(resultSet, metadata);
        }, true);
    }

    public <T extends Entity> Optional<T> findById(Long id, Class<T> entityClazz) {
        EntityMetadata metadata = metadataMap.get(entityClazz);
        SqlRequest<T> request = sqlRequestGenerator.findById(metadata);
        return perform(conn -> {
            PreparedStatement preparedStatement =
                    conn.prepareStatement(request.getSql());
            request.insertValues(preparedStatement, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultInterpreter.interpret(resultSet, metadata);
        }, true);
    }

    private <R> R perform(ExceptionAllowsFunction<Connection, R> statementFunction,
                          boolean readOnly) {
        try (Connection connection = dataSource.getConnection()) {
            connection.beginRequest();
            connection.setReadOnly(readOnly);

            R result = statementFunction.apply(connection);

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
