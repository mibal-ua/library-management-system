package ua.mibal.minervaTest.frameworks.orm.component;

import ua.mibal.minervaTest.frameworks.context.annotations.Component;
import ua.mibal.minervaTest.frameworks.orm.model.EntityMetadata;
import ua.mibal.minervaTest.frameworks.orm.model.SqlRequest;

import java.util.Collections;

/**
 * @author Mykhailo Balakhon
 * @link <a href="mailto:9mohapx9@gmail.com">9mohapx9@gmail.com</a>
 */
@Component
public class SqlRequestGenerator {

    public <T> SqlRequest<T> save(EntityMetadata entityMetadata) {
        String fields = String.join(",", entityMetadata.getValueColumns());
        String values = String.join(",", Collections.nCopies(fields.length(), "?"));
        return null;
    }

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
