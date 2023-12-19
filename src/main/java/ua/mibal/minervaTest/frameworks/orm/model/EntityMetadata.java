package ua.mibal.minervaTest.frameworks.orm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class EntityMetadata {

    private final List<Field> simpleFields;
    private final List<Relation> relations;
    private final List<String> columns;
    private final String table;

    public EntityMetadata(List<Field> simpleFields, List<Relation> relations, String table) {
        this.simpleFields = simpleFields;
        this.relations = relations;
        this.table = table;
        this.columns = initColumns();
    }

    private List<String> initColumns() {
        List<String> simpleColumns = simpleFields.stream()
                .map(f -> Optional.ofNullable(f.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElseGet(() -> {
                            if (!f.isAnnotationPresent(Id.class)) {
                                throw new IllegalArgumentException("Cannot define column name for field " + f);
                            }
                            return f.getName();
                        }))
                .toList();
        List<String> relationColumns = relations.stream()
                .filter(rel -> rel.getRelationType().annotationType().equals(ManyToOne.class))
                .map(rel -> rel.getField().getAnnotation(JoinColumn.class).name())
                .toList();
        List<String> allColumns = new ArrayList<>(simpleColumns);
        allColumns.addAll(relationColumns);
        return allColumns;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<String> getValueColumns() {
        String idField = simpleFields.stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .map(Field::getName)
                .findFirst()
                .orElseThrow();
        ArrayList<String> columnsWithoutId = new ArrayList<>(getColumns());
        columnsWithoutId.remove(idField);
        return columnsWithoutId;
    }

    public List<Field> getSimpleFields() {
        return simpleFields;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public String getTable() {
        return table;
    }
}
