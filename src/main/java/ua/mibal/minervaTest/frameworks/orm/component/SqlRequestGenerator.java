package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.orm.model.Column;
import ua.mibal.minervaTest.frameworks.orm.model.EntityMetadata;
import ua.mibal.minervaTest.frameworks.orm.model.SqlRequest;
import ua.mibal.minervaTest.model.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
@Component
public class SqlRequestGenerator {

    private static Object getEntityId(Object relatedEntity) {
        if (relatedEntity instanceof Entity e) {
            return e.getId();
        } else if (relatedEntity == null) {
            return null;
        } else {
            throw new RuntimeException(
                    "Entity field relation must be linked with Object " +
                    "that extends " + Entity.class.getName()
            );
        }
    }

    private static String generateSaveSql(EntityMetadata entityMetadata) {
        List<String> columnNames = entityMetadata.getColumnNames();
        String fields = String.join(",", columnNames);
        String values = String.join(",", Collections.nCopies(columnNames.size(), "?"));
        return "insert into %s(%s) value(%s)".formatted(
                entityMetadata.getTable(),
                fields,
                values
        );
    }

    private static <T> List<Function<T, Object>> generateValuesDeleteInjections(EntityMetadata entityMetadata) {
        Column idColumn = entityMetadata.getIdColumn();
        return List.of(idColumn::getValue);
    }

    private static String generateDeleteSql(EntityMetadata entityMetadata) {
        return ("delete desiredEntityToDelete " +
                "from %s desiredEntityToDelete " +
                "where %s = ?").formatted(
                entityMetadata.getTable(), entityMetadata.getId()
        );
    }

    public <T> SqlRequest<T> save(EntityMetadata entityMetadata, IdProvider idProvider) {
        String sql = generateSaveSql(entityMetadata);
        List<Function<T, Object>> valuesInjections =
                generateValuesSaveInjections(entityMetadata, idProvider);
        return new SqlRequest<>(sql, valuesInjections);
    }

    private <T> List<Function<T, Object>> generateValuesSaveInjections(EntityMetadata entityMetadata,
                                                                       IdProvider idProvider) {
        List<Function<T, Object>> valueInjectionProviders = new ArrayList<>();
        for (Column column : entityMetadata.getColumns()) {
            if (column.isId()) {
                valueInjectionProviders.add((T entity) ->
                        Optional.ofNullable(column.getValue(entity))
                            .orElseGet(() -> idProvider.getId(entityMetadata)));
            } else if (column.isRelation()) {
                valueInjectionProviders.add((T entity) -> {
                    Object relatedEntity = column.getValue(entity);
                    return getEntityId(relatedEntity);
                });
            } else {
                valueInjectionProviders.add(column::getValue);
            }
        }
        return valueInjectionProviders;

    }

    public <T> SqlRequest<T> findAll(EntityMetadata entityMetadata) {
        // TODO
        return null;
    }

    public <T> SqlRequest<T> delete(EntityMetadata entityMetadata) {
        String sql = generateDeleteSql(entityMetadata);
        List<Function<T, Object>> valuesInjections = generateValuesDeleteInjections(entityMetadata);
        return new SqlRequest<>(sql, valuesInjections);
    }

    public <T> SqlRequest<T> findById(EntityMetadata entityMetadata) {
        // TODO
        return null;
    }
}
