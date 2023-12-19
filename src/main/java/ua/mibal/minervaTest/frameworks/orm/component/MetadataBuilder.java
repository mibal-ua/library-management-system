package ua.mibal.minervaTest.frameworks.orm.component;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import ua.mibal.minervaTest.frameworks.orm.model.EntityMetadata;
import ua.mibal.minervaTest.frameworks.orm.model.Relation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
public class MetadataBuilder {
    private static void validateEntity(Class<?> entityClazz) {
        if (!entityClazz.isAnnotationPresent(jakarta.persistence.Entity.class)) {
            throw new IllegalArgumentException(
                    "Entity " + entityClazz + " has no '@Entity' annotation present");
        }
    }

    private static String tableName(Class<?> entityClazz) {
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

    public EntityMetadata build(Class<?> clazz) {
        validateEntity(clazz);

        List<Field> simpleFields = new ArrayList<>();
        List<Relation> relationFields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (isSimpleDataField(field)) {
                simpleFields.add(field);
            } else if (isRelationField(field)) {
                Annotation relationType = relationTypeFrom(field);
                Relation relation = new Relation(field, relationType);
                relationFields.add(relation);
            } else {
                throw new IllegalStateException("Field " + field + " on entity " + clazz +
                                                " is not mapped column or relation");
            }
        }
        return new EntityMetadata(simpleFields, relationFields, tableName(clazz));
    }
}
