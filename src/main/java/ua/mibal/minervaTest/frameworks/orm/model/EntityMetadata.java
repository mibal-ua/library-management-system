package ua.mibal.minervaTest.frameworks.orm.model;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class EntityMetadata {

    private final List<Field> simpleFields;
    private final List<Relation> relations;
    private final String table;

    public EntityMetadata(List<Field> simpleFields, List<Relation> relations, String table) {
        this.simpleFields = simpleFields;
        this.relations = relations;
        this.table = table;
    }
}
