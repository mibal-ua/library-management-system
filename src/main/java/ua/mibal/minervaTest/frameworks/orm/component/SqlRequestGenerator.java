package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.orm.model.Column;
import ua.mibal.minervaTest.frameworks.orm.model.EntityMetadata;
import ua.mibal.minervaTest.frameworks.orm.model.SqlRequest;
import ua.mibal.minervaTest.model.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    public <T> SqlRequest<T> save(EntityMetadata entityMetadata) {
        String sql = generateSaveSql(entityMetadata);
        List<Function<T, Object>> valuesInjections =
                generateValuesSaveInjections(entityMetadata);
        return new SqlRequest<>(sql, valuesInjections);
    }

    private <T> List<Function<T, Object>> generateValuesSaveInjections(EntityMetadata entityMetadata) {
        List<Function<T, Object>> valueInjectionProviders = new ArrayList<>();
        for (Column column : entityMetadata.getColumns()) {
            if (column.isRelation()) {
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

    // TODO

    public <T> SqlRequest<T> findAll(EntityMetadata entityMetadata) {
        return null;
    }

    public <T> SqlRequest<T> delete(EntityMetadata entityMetadata) {
        return null;
    }

    public <T> SqlRequest<T> findById(EntityMetadata entityMetadata) {
        return null;
    }
}