package ua.mibal.minervaTest.frameworks.orm.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class Relation {

    private final Field field;
    private final Annotation relationType;

    public Relation(Field field, Annotation relationType) {
        this.field = field;
        this.relationType = relationType;
    }

    public Field getField() {
        return field;
    }

    public Annotation getRelationType() {
        return relationType;
    }

    public String getColumnName() {
        if (relationType instanceof ManyToOne) {
            return field.getAnnotation(JoinColumn.class)
                    .name();
        } else if (relationType instanceof OneToMany) {
            return null;
        } else {
            throw new IllegalArgumentException(
                    "Cannot define column name for relation field " + field
            );
        }
    }
}
