package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.orm.model.EntityMetadata;

import java.util.Collections;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
@Component
public class SqlRequestGenerator {

    public String save(EntityMetadata entityMetadata) {
        String fields = String.join(",", entityMetadata.getValueColumns());
        String values = String.join(",", Collections.nCopies(fields.length(), "?"));
        return "insert into %s(%s) value(%s)".formatted(
                entityMetadata.getTable(),
                fields,
                values
        );
    }

    public String findAll() {
        return null;
    }

    public String delete(EntityMetadata entityMetadata) {
        return "delete from %s where %s.%s = ?".formatted(
                entityMetadata.getTable(),
                entityMetadata.getTable(),
                entityMetadata.getIdColumn()
        );
    }

    public String findById(Long id) {
        return null;
    }
}
