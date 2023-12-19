package ua.mibal.minervaTest.frameworks.orm.model;

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
}
