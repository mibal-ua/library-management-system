package ua.mibal.minervaTest.frameworks.orm.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import ua.mibal.minervaTest.model.Entity;

import java.lang.annotation.Annotation;
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
    private final String table;

    public EntityMetadata(List<Field> simpleFields, List<Relation> relations, String table) {
        this.simpleFields = simpleFields;
        this.relations = relations;
        this.table = table;
    }

    public static <T extends Entity> EntityMetadata of(Class<T> entityClazz) {
        validateEntity(entityClazz);

        List<Field> simpleFields = new ArrayList<>();
        List<Relation> relationFields = new ArrayList<>();
        for (Field field : entityClazz.getDeclaredFields()) {
            if (isSimpleDataField(field)) {
                simpleFields.add(field);
            } else if (isRelationField(field)) {
                Annotation relationType = relationTypeFrom(field);
                Relation relation = new Relation(field, relationType);
                relationFields.add(relation);
            } else {
                throw new IllegalStateException("Field " + field + " on entity " + entityClazz +
                                                " is not mapped column or relation");
            }
        }
        return new EntityMetadata(simpleFields, relationFields, tableName(entityClazz));
    }

    private static void validateEntity(Class<?> entityClazz) {
        if (!entityClazz.isAnnotationPresent(jakarta.persistence.Entity.class)) {
            throw new IllegalArgumentException(
                    "Entity " + entityClazz + " has no '@Entity' annotation present");
        }
    }

    private static <T extends Entity> String tableName(Class<T> entityClazz) {
        return Optional.ofNullable(entityClazz.getAnnotation(Table.class))
                .map(Table::name)
                .orElse(entityClazz.getSimpleName().toLowerCase());
    }

    private static boolean isSimpleDataField(Field field) {
        return field.isAnnotationPresent(Column.class) ||
               field.isAnnotationPresent(Id.class);
    }

    private static boolean isRelationField(Field field) {
        return field.isAnnotationPresent(OneToOne.class) ||
               field.isAnnotationPresent(OneToMany.class) ||
               field.isAnnotationPresent(ManyToOne.class);
    }

    private static Annotation relationTypeFrom(Field field) {
        if (field.isAnnotationPresent(OneToOne.class)) {
            return field.getAnnotation(OneToOne.class);
        }
        if (field.isAnnotationPresent(OneToMany.class)) {
            return field.getAnnotation(OneToMany.class);
        }
        if (field.isAnnotationPresent(ManyToOne.class)) {
            return field.getAnnotation(ManyToOne.class);
        }
        throw new IllegalArgumentException("Unmapped entity relation field " + field);
    }
}
