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
        StringBuilder requestBuilder = new StringBuilder("insert into client ");

        String fields = String.join(",", entityMetadata.getValueColumns());
        requestBuilder.append("(")
                .append(fields)
                .append(")");
        requestBuilder.append("value (")
                .append(String.join(",", Collections.nCopies(fields.length(), "?")))
                .append(")");

        return requestBuilder.toString();
    }

    public String findAll() {
        return null;
    }

    public String delete(Object entity) {
        return null;
    }

    public String findById(Long id) {
        return null;
    }
}
