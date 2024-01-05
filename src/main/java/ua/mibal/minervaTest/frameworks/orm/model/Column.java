package ua.mibal.minervaTest.frameworks.orm.model;

import java.lang.reflect.Field;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class Column {

    private final Field field;
    private final String name;
    private final boolean relation;

    public Column(Field field, String name, boolean relation) {
        this.field = field;
        this.name = name;
        this.relation = relation;
    }

    public String getName() {
        return name;
    }

    public boolean isRelation() {
        return relation;
    }

    public <T> Object getValue(T entity) {
        try {
            field.setAccessible(true);
            Object value = field.get(entity);
            field.setAccessible(false);
            return value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
