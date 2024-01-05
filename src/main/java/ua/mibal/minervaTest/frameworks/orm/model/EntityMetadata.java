package ua.mibal.minervaTest.frameworks.orm.model;

import jakarta.persistence.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class EntityMetadata {

    private final String table;
    private final List<Column> columns;
    private final Column idColumn;

    public EntityMetadata(List<Field> simpleFields,
                          List<Relation> relationFields,
                          String table) {
        this.table = table;
        this.columns = initAllColumns(simpleFields, relationFields);
        this.idColumn = initIdColumn(simpleFields);
    }

    private Column initIdColumn(List<Field> simpleFields) {
        return simpleFields.stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .map(field -> new Column(
                        field,
                        getColumnName(field),
                        false,
                        true
                ))
                .findFirst()
                .orElseThrow();
    }

    private List<Column> initAllColumns(List<Field> simpleFields,
                                        List<Relation> relationFields) {
        List<Column> simpleColumns = simpleFields.stream()
                .map(field -> new Column(
                        field,
                        getColumnName(field),
                        false,
                        field.isAnnotationPresent(Id.class)
                ))
                .toList();
        List<Column> relationColumns = relationFields.stream()
                .map(relation -> new Column(relation.getField(), relation.getColumnName(), true, false))
                .filter(column -> column.getName() != null)
                .toList();

        List<Column> union = new ArrayList<>();
        union.addAll(simpleColumns);
        union.addAll(relationColumns);
        return union;
    }

    private String getColumnName(Field field) {
        if (field.isAnnotationPresent(jakarta.persistence.Column.class)) {
            return field.getAnnotation(jakarta.persistence.Column.class).name();
        } else if (field.isAnnotationPresent(Id.class)) {
            return field.getName();
        } else {
            throw new IllegalArgumentException(
                    "Cannot define column name for field " + field +
                    " at class " + field.getDeclaringClass()
            );
        }
    }

    public List<String> getColumnNames() {
        return columns.stream()
                .map(Column::getName)
                .filter(Objects::nonNull)
                .toList();
    }

    public String getTable() {
        return table;
    }

    public String getId() {
        return idColumn.getName();
    }

    public List<Column> getColumns() {
        return columns;
    }

    public Column getIdColumn() {
        return idColumn;
    }
}
